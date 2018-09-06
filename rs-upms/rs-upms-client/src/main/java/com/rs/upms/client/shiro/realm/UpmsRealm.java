package com.rs.upms.client.shiro.realm;

import com.reger.dubbo.annotation.Inject;
import com.rs.common.util.MD5Util;
import com.rs.common.util.PropertyUtil;
import com.rs.upms.client.shiro.util.ShiroUtil;
import com.rs.upms.common.constant.UpmsConstant;
import com.rs.upms.dao.model.UpmsPermission;
import com.rs.upms.dao.model.UpmsRole;
import com.rs.upms.dao.model.UpmsUser;
import com.rs.upms.rpc.api.UpmsApiService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户认证和授权
 *
 * @author liegou
 * @date
 */
@Component
public class UpmsRealm extends AuthorizingRealm {

    private static Logger _log = LoggerFactory.getLogger(UpmsRealm.class);
    @Inject
    private UpmsApiService upmsApiService;
    /**
     * 类型 server 服务端，client 客户端
     */
    private String upmsType = PropertyUtil.getProperty("rs.upms.type");

    /**
     * 授权：验证权限时调用
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = ShiroUtil.getUpmsUser().getUserName();
        UpmsUser upmsUser = upmsApiService.selectUpmsUserByUsername(userName);
        // 当前用户所有角色
        List<UpmsRole> upmsRoles = upmsApiService.selectUpmsRoleByUpmsUserId(upmsUser.getUserId());
        Set<String> roles = new HashSet<>();
        for (UpmsRole upmsRole : upmsRoles) {
            if (StringUtils.isNotBlank(upmsRole.getName())) {
                roles.add(upmsRole.getName());
            }
        }

        // 当前用户所有权限
        List<UpmsPermission> upmsPermissions = upmsApiService.selectUpmsPermissionByUpmsUserId(upmsUser.getUserId());
        Set<String> permissions = new HashSet<>();
        for (UpmsPermission upmsPermission : upmsPermissions) {
            if (StringUtils.isNotBlank(upmsPermission.getPermissionValue())) {
                permissions.add(upmsPermission.getPermissionValue());
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证：登录时调用
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        UpmsUser upmsUser = upmsApiService.selectUpmsUserByUsername(username);
        // client无密认证
        if (UpmsConstant.UPMS_TYPE_CLIENT.equals(upmsType)) {
            return new SimpleAuthenticationInfo(upmsUser, password, getName());
        }
        if (null == upmsUser) {
            throw new UnknownAccountException();
        }
        if (!upmsUser.getPassword().equals(MD5Util.MD5(password + upmsUser.getSalt()))) {
            throw new IncorrectCredentialsException();
        }
        if (upmsUser.getLocked() == 1) {
            throw new LockedAccountException();
        }

        return new SimpleAuthenticationInfo(upmsUser, password, getName());
    }

}
