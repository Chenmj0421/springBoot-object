package com.rs.upms.client.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.rs.common.redis.RedisService;
import com.rs.common.util.RequestParameterUtil;
import com.rs.common.util.httpclient.HttpHelper;
import com.rs.upms.client.shiro.session.UpmsSessionDao;
import com.rs.upms.common.constant.UpmsConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 重写authc过滤器
 *
 * @author liegou
 * @date
 */
public class UpmsAuthenticationFilter extends AuthenticationFilter {

    private final static Logger log = LoggerFactory.getLogger(UpmsAuthenticationFilter.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    UpmsSessionDao upmsSessionDao;

    /**
     * 类型 server 服务端，client 客户端
     */
    @Value("${rs.upms.type}")
    public String upmsType;
    /**
     * 统一登录地址
     */
    @Value("${rs.upms.sso.server.url}")
    public String rsUpmsSsoServerUrl;
    /**
     * 应用id ，与应用名称保持一致
     */
    @Value("${spring.application.name}")
    public String appId;

    /**
     * 局部会话key
     */
    private final static String RS_UPMS_CLIENT_SESSION_ID = UpmsConstant.RS_UPMS_CLIENT_SESSION_ID;
    /**
     * 单点同一个code所有局部会话key
     */
    private final static String RS_UPMS_CLIENT_SESSION_IDS = UpmsConstant.RS_UPMS_CLIENT_SESSION_IDS;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        // 判断请求类型
        session.setAttribute(UpmsConstant.UPMS_TYPE, upmsType);
        if (UpmsConstant.UPMS_TYPE_CLIENT.equals(upmsType)) {
            return validateClient(request, response);
        }
        if (UpmsConstant.UPMS_TYPE_SERVER.equals(upmsType)) {
            return subject.isAuthenticated();
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        StringBuffer ssoServerUrl = new StringBuffer(rsUpmsSsoServerUrl);
        // server需要登录
        if (UpmsConstant.UPMS_TYPE_SERVER.equals(upmsType)) {
            WebUtils.toHttp(response).sendRedirect(ssoServerUrl.toString());
            return false;
        }
        ssoServerUrl.append("/sso/index").append("?").append("appid").append("=").append(appId);
        // 回跳地址
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        StringBuffer backurl = httpServletRequest.getRequestURL();
        String queryString = httpServletRequest.getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            backurl.append("?").append(queryString);
        }
        ssoServerUrl.append("&").append("backurl").append("=").append(URLEncoder.encode(backurl.toString(), "utf-8"));
        WebUtils.toHttp(response).sendRedirect(ssoServerUrl.toString());
        return false;
    }

    /**
     * 认证中心登录成功带回code
     *
     * @param request
     */
    private boolean validateClient(ServletRequest request, ServletResponse response) {
        Subject subject = getSubject(request, response);
        Session session = subject.getSession();
        String sessionId = session.getId().toString();
        Long timeOut = session.getTimeout() / 1000;
        // 判断局部会话是否登录
        String cacheClientSession = redisService.get(RS_UPMS_CLIENT_SESSION_ID + "_" + session.getId());
        if (StringUtils.isNotBlank(cacheClientSession)) {
            // 更新code有效期
            redisService.set(RS_UPMS_CLIENT_SESSION_ID + "_" + sessionId, cacheClientSession, timeOut);
            redisService.expire(RS_UPMS_CLIENT_SESSION_IDS + "_" + cacheClientSession, timeOut);
            // 移除url中的code参数
            if (null != request.getParameter("code")) {
                String backUrl = RequestParameterUtil.getParameterWithOutCode(WebUtils.toHttp(request));
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                try {
                    httpServletResponse.sendRedirect(backUrl);
                } catch (IOException e) {
                    log.error("局部会话已登录，移除code参数跳转出错：", e);
                }
            } else {
                return true;
            }
        }
        // 判断是否有认证中心code
        String code = request.getParameter("upms_code");
        // 已拿到code
        if (StringUtils.isNotBlank(code)) {
            // HttpPost去校验code
            try {
                StringBuffer ssoServerUrl = new StringBuffer(rsUpmsSsoServerUrl);
                HttpClient httpclient = HttpHelper.createHttpClient();
                HttpPost httpPost = new HttpPost(ssoServerUrl.toString() + "/sso/code");

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("code", code));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpclient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity httpEntity = httpResponse.getEntity();
                    JSONObject result = JSONObject.parseObject(EntityUtils.toString(httpEntity));
                    if (1 == result.getIntValue("code") && result.getString("data").equals(code)) {
                        // code校验正确，创建局部会话
                        redisService.set(RS_UPMS_CLIENT_SESSION_ID + "_" + sessionId, code, timeOut);
                        // 保存code对应的局部会话sessionId，方便退出操作
                        redisService.sadd(RS_UPMS_CLIENT_SESSION_IDS + "_" + code, sessionId);
                        redisService.expire(RS_UPMS_CLIENT_SESSION_IDS + "_" + code, timeOut);
                        log.debug("当前code={}，对应的注册系统个数：{}个", code, redisService.ssize(RS_UPMS_CLIENT_SESSION_IDS + "_" + code));
                        // 移除url中的token参数
                        String backUrl = RequestParameterUtil.getParameterWithOutCode(WebUtils.toHttp(request));
                        // 返回请求资源
                        try {
                            // client无密认证
                            String username = request.getParameter("upms_username");
                            subject.login(new UsernamePasswordToken(username, ""));
                            HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                            httpServletResponse.sendRedirect(backUrl);
                            return true;
                        } catch (IOException e) {
                            log.error("已拿到code，移除code参数跳转出错：", e);
                        }
                    } else {
                        log.warn(result.getString("data"));
                    }
                }
            } catch (IOException e) {
                log.error("验证token失败：", e);
            }
        }
        return false;
    }

}
