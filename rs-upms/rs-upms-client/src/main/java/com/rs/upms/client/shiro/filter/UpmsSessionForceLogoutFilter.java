package com.rs.upms.client.shiro.filter;

import com.rs.upms.client.shiro.session.UpmsSessionDao;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 强制退出会话过滤器
 *
 * @author liegou
 * @date
 */
public class UpmsSessionForceLogoutFilter extends AccessControlFilter {
    private String kickOutUrl; //踢出后到的地址
    private boolean kickOutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1
    @Autowired
    UpmsSessionDao upmsSessionDao;
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Session session = getSubject(request, response).getSession(false);
        if (session == null) {
            return true;
        }
        boolean forceout = session.getAttribute("FORCE_LOGOUT") == null;
        return forceout;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        getSubject(request, response).logout();
        String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";
        WebUtils.issueRedirect(request, response, loginUrl);
        return false;
    }

}
