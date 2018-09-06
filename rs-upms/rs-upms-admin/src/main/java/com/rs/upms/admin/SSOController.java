package com.rs.upms.admin;


import com.reger.dubbo.annotation.Inject;
import com.rs.common.base.BaseController;
import com.rs.common.base.BaseResult;
import com.rs.common.base.BaseResultConstant;
import com.rs.common.exception.MyException;
import com.rs.common.redis.RedisService;
import com.rs.upms.client.shiro.session.UpmsSession;
import com.rs.upms.client.shiro.session.UpmsSessionDao;
import com.rs.upms.client.shiro.util.ShiroUtil;
import com.rs.upms.common.constant.UpmsConstant;
import com.rs.upms.dao.model.UpmsSystemExample;
import com.rs.upms.rpc.api.UpmsSystemService;
import com.rs.upms.rpc.api.UpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 单点登录管理
 *
 * @author liegou
 */
@Controller
@RequestMapping("/sso")
@Api(value = "单点登录管理", description = "单点登录管理")
public class SSOController extends BaseController {

    private final static Logger _log = LoggerFactory.getLogger(SSOController.class);
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

    @Inject
    UpmsSystemService upmsSystemService;

    @Inject
    UpmsUserService upmsUserService;

    @Autowired
    UpmsSessionDao upmsSessionDao;
    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "认证中心首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(HttpServletRequest request) throws Exception {
        String appId = request.getParameter("appid");
        String backurl = request.getParameter("backurl");
        if (StringUtils.isBlank(appId)) {
            throw new MyException(BaseResultConstant.FAILED.getCode(), "无效的访问！");
        }
        // 判断请求认证系统是否注册
        UpmsSystemExample upmsSystemExample = new UpmsSystemExample();
        upmsSystemExample.createCriteria()
                .andNameEqualTo(appId);
        int count = upmsSystemService.countByExample(upmsSystemExample);
        if (0 == count) {
            throw new MyException(BaseResultConstant.UNREGISTERSYSTEM_EXCEPTION.getCode(), String.format(BaseResultConstant.UNREGISTERSYSTEM_EXCEPTION.getMessage() + ":%s", appId));
        }
        if (backurl == null) {
            backurl = "/";
        }
        return "redirect:/sso/login?backurl=" + URLEncoder.encode(backurl, "utf-8");
    }

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request) {
        String serverSessionId = ShiroUtil.getSession().getId().toString();
        // 判断是否已登录，如果已登录，则回跳
        String code = redisService.get(RS_UPMS_SERVER_SESSION_ID + "_" + serverSessionId);
        // code校验值
        if (StringUtils.isNotBlank(code)) {
            // 回跳
            String backurl = request.getParameter("backurl");
            String username = ShiroUtil.getUpmsUser().getUserName();
            if (StringUtils.isBlank(backurl)) {
                backurl = "/";
            } else {
                if (backurl.contains("?")) {
                    backurl += "&upms_code=" + code + "&upms_username=" + username;
                } else {
                    backurl += "?upms_code=" + code + "&upms_username=" + username;
                }
            }
            _log.debug("认证中心帐号通过，带code回跳：{}", backurl);
            return "redirect:" + backurl;
        }
        return "index";
    }

    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        if (StringUtils.isBlank(username)) {
            return BaseResult.newFailure(BaseResultConstant.EMPTY_USERNAME.getCode(), BaseResultConstant.EMPTY_USERNAME.getMessage());
        }
        if (StringUtils.isBlank(password)) {
            return BaseResult.newFailure(BaseResultConstant.EMPTY_PASSWORD.getCode(), BaseResultConstant.EMPTY_PASSWORD.getMessage());
        }
        Subject subject = ShiroUtil.getSubject();
        String sessionId = ShiroUtil.getSession().getId().toString();
        // 判断是否已登录，如果已登录，则回跳，防止重复登录
        String hasCode = redisService.get(RS_UPMS_SERVER_SESSION_ID + "_" + sessionId);
        // code校验值
        if (StringUtils.isBlank(hasCode)) {
            // 使用shiro认证
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            try {
                if (BooleanUtils.toBoolean(rememberMe)) {
                    usernamePasswordToken.setRememberMe(true);
                } else {
                    usernamePasswordToken.setRememberMe(true);
                }
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                return BaseResult.newFailure(BaseResultConstant.INVALID_USERNAME.getCode(), BaseResultConstant.INVALID_USERNAME.getMessage());
            } catch (IncorrectCredentialsException e) {
                return BaseResult.newFailure(BaseResultConstant.INVALID_PASSWORD.getCode(), BaseResultConstant.INVALID_PASSWORD.getMessage());
            } catch (LockedAccountException e) {
                return BaseResult.newFailure(BaseResultConstant.INVALID_ACCOUNT.getCode(), BaseResultConstant.INVALID_ACCOUNT.getMessage());
            }
            // 更新session状态
            upmsSessionDao.updateStatus(sessionId, UpmsSession.OnlineStatus.on_line);
            // 全局会话sessionId列表，供会话管理
            redisService.lpush(RS_UPMS_SERVER_SESSION_IDS, sessionId);
            // 默认验证帐号密码正确，创建code
            String code = UUID.randomUUID().toString();
            // 全局会话的code
            redisService.set(RS_UPMS_SERVER_SESSION_ID + "_" + sessionId, code, subject.getSession().getTimeout() / 1000);
            // code校验值
            redisService.set(RS_UPMS_SERVER_CODE + "_" + code, code, subject.getSession().getTimeout() / 1000);
        }
        // 回跳登录前地址
        String backurl = request.getParameter("backurl");
        if (StringUtils.isBlank(backurl)) {
            return BaseResult.newSuccess("/");
        } else {
            return BaseResult.newSuccess(backurl);
        }
    }

    @ApiOperation(value = "校验code")
    @RequestMapping(value = "/code", method = RequestMethod.POST)
    @ResponseBody
    public Object code(HttpServletRequest request) {
        String codeParam = request.getParameter("code");
        String code = redisService.get(RS_UPMS_SERVER_CODE + "_" + codeParam).toString();
        if (StringUtils.isBlank(codeParam) || !codeParam.equals(code)) {
            return BaseResult.newFailure(BaseResultConstant.FAILED.getCode(), "无效code");
        }
        return BaseResult.newSuccess(code);
    }

    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        // shiro退出登录
        SecurityUtils.getSubject().logout();
        // 跳回原地址
        String redirectUrl = request.getHeader("Referer");
        if (null == redirectUrl) {
            redirectUrl = "/";
        }
        return "redirect:" + redirectUrl;
    }

}