package com.rs.upms.client.shiro.session;

import com.rs.common.redis.RedisService;
import com.rs.common.util.SerializableUtil;
import com.rs.upms.common.constant.UpmsConstant;
import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * 基于redis的sessionDao，缓存共享session
 *
 * @author liegou
 * @date
 */
@Component
public class UpmsSessionDao extends CachingSessionDAO {

    private static Logger log = LoggerFactory.getLogger(UpmsSessionDao.class);

    @Autowired
    private RedisService redisService;
    /**
     * 会话key
     */
    private final static String RS_UPMS_SHIRO_SESSION_ID = UpmsConstant.RS_UPMS_SHIRO_SESSION_ID;
    /**
     * 全局会话key
     */
    private final static String RS_UPMS_SERVER_SESSION_ID = UpmsConstant.RS_UPMS_SERVER_SESSION_ID;
    /**
     * 全局会话列表key
     */
    private final static String RS_UPMS_SERVER_SESSION_IDS = UpmsConstant.RS_UPMS_SERVER_SESSION_IDS;
    /**
     * code key
     */
    private final static String RS_UPMS_SERVER_CODE = UpmsConstant.RS_UPMS_SERVER_CODE;
    /**
     * 局部会话key
     */
    private final static String RS_UPMS_CLIENT_SESSION_ID = UpmsConstant.RS_UPMS_CLIENT_SESSION_ID;
    /**
     * 单点同一个code所有局部会话key
     */
    private final static String RS_UPMS_CLIENT_SESSION_IDS = UpmsConstant.RS_UPMS_CLIENT_SESSION_IDS;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        redisService.set(RS_UPMS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(session), session.getTimeout() / 1000);
        log.debug("doCreate >>>>> sessionId={}", sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String session = redisService.get(RS_UPMS_SHIRO_SESSION_ID + "_" + sessionId);
        log.debug("doReadSession >>>>> sessionId={}", sessionId);
        return SerializableUtil.deserialize(session);
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            return;
        }
        // 更新session的最后一次访问时间
        UpmsSession upmsSession = (UpmsSession) session;
        UpmsSession cacheUpmsSession = (UpmsSession) doReadSession(session.getId());
        if (null != cacheUpmsSession) {
            upmsSession.setStatus(cacheUpmsSession.getStatus());
            upmsSession.setAttribute("FORCE_LOGOUT", cacheUpmsSession.getAttribute("FORCE_LOGOUT"));
        }
        redisService.set(RS_UPMS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), session.getTimeout() / 1000);
        // 更新RS_UPMS_SERVER_SESSION_ID、RS_UPMS_SERVER_CODE过期时间 TODO
        redisService.expire(RS_UPMS_SERVER_SESSION_ID,session.getTimeout() / 1000);
        redisService.expire(RS_UPMS_SERVER_CODE,session.getTimeout() / 1000);
        log.debug("doUpdate >>>>> sessionId={}", session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
        String upmsType = ObjectUtils.toString(session.getAttribute(UpmsConstant.UPMS_TYPE));
        if (UpmsConstant.UPMS_TYPE_CLIENT.equals(upmsType)) {
            // 删除局部会话和同一code注册的局部会话
            String code = redisService.get(RS_UPMS_CLIENT_SESSION_ID + "_" + sessionId);
            redisService.del(RS_UPMS_CLIENT_SESSION_ID + "_" + sessionId);
            redisService.srem(RS_UPMS_CLIENT_SESSION_IDS + "_" + code, sessionId);
        }
        if (UpmsConstant.UPMS_TYPE_SERVER.equals(upmsType)) {
            // 当前全局会话code
            String code = redisService.get(RS_UPMS_SERVER_SESSION_ID + "_" + sessionId);
            // 清除全局会话
            redisService.del(RS_UPMS_SERVER_SESSION_ID + "_" + sessionId);
            // 清除code校验值
            redisService.del(RS_UPMS_SERVER_CODE + "_" + code);
            // 清除所有局部会话
            Set<String> clientSessionIds = redisService.smembers(RS_UPMS_CLIENT_SESSION_IDS + "_" + code);
            for (String clientSessionId : clientSessionIds) {
                redisService.del(RS_UPMS_CLIENT_SESSION_ID + "_" + clientSessionId);
                redisService.srem(RS_UPMS_CLIENT_SESSION_IDS + "_" + code, clientSessionId);
            }
            log.debug("当前code={}，对应的注册系统个数：{}个", code, redisService.ssize(RS_UPMS_CLIENT_SESSION_IDS + "_" + code));
            // 维护会话id列表，提供会话分页管理
            redisService.lrem(RS_UPMS_SERVER_SESSION_IDS, 1L, sessionId);
        }
        // 删除session
        redisService.del(RS_UPMS_SHIRO_SESSION_ID + "_" + sessionId);
        log.debug("doUpdate >>>>> sessionId={}", sessionId);
    }

    /**
     * 获取会话列表
     *
     * @param offset
     * @param limit
     * @return
     */
    public Map getActiveSessions(Long offset, Long limit) {
        Map sessions = new HashMap();
        // 获取在线会话总数
        long total = redisService.llen(RS_UPMS_SERVER_SESSION_IDS);
        // 获取当前页会话详情
        List<String> ids = redisService.lrange(RS_UPMS_SERVER_SESSION_IDS, offset, (offset + limit - 1));
        List<Session> rows = new ArrayList<>();
        for (String id : ids) {
            String session = redisService.get(RS_UPMS_SHIRO_SESSION_ID + "_" + id);
            // 过滤redis过期session
            if (null == session) {
                redisService.lrem(RS_UPMS_SERVER_SESSION_IDS, 1L, id);
                total = total - 1;
                continue;
            }
            rows.add(SerializableUtil.deserialize(session));
        }
        sessions.put("total", total);
        sessions.put("rows", rows);
        return sessions;
    }

    /**
     * 强制退出
     *
     * @param ids
     * @return
     */
    public int forceout(String ids) {
        String[] sessionIds = ids.split(",");
        for (String sessionId : sessionIds) {
            // 会话增加强制退出属性标识，当此会话访问系统时，判断有该标识，则退出登录
            String session = redisService.get(RS_UPMS_SHIRO_SESSION_ID + "_" + sessionId);
            UpmsSession upmsSession = (UpmsSession) SerializableUtil.deserialize(session);
            upmsSession.setStatus(UpmsSession.OnlineStatus.force_logout);
            upmsSession.setAttribute("FORCE_LOGOUT", "FORCE_LOGOUT");
            redisService.set(RS_UPMS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(upmsSession), upmsSession.getTimeout() / 1000);
        }
        return sessionIds.length;
    }

    /**
     * 更改在线状态
     *
     * @param sessionId
     * @param onlineStatus
     */
    public void updateStatus(Serializable sessionId, UpmsSession.OnlineStatus onlineStatus) {
        UpmsSession session = (UpmsSession) doReadSession(sessionId);
        if (null == session) {
            return;
        }
        session.setStatus(onlineStatus);
        redisService.set(RS_UPMS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), session.getTimeout() / 1000);
    }

}
