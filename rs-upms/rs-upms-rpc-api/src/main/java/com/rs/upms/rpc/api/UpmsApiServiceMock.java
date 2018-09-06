package com.rs.upms.rpc.api;

import com.rs.upms.dao.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *
 * 降级实现UpmsApiService接口
 * @author liegou
 * @date
 */
public class UpmsApiServiceMock implements UpmsApiService {

    private static Logger log = LoggerFactory.getLogger(UpmsApiServiceMock.class);


    @Override
    public List<UpmsPermission> selectUpmsPermissionByUpmsUserId(String upmsUserId) {
        return null;
    }

    @Override
    public List<UpmsPermission> selectUpmsPermissionByUpmsUserIdByCache(String upmsUserId) {
        return null;
    }

    @Override
    public List<UpmsRole> selectUpmsRoleByUpmsUserId(String upmsUserId) {
        return null;
    }

    @Override
    public List<UpmsRole> selectUpmsRoleByUpmsUserIdByCache(String upmsUserId) {
        return null;
    }

    @Override
    public List<UpmsRolePermission> selectUpmsRolePermisstionByUpmsRoleId(String upmsRoleId) {
        return null;
    }

    @Override
    public List<UpmsUserPermission> selectUpmsUserPermissionByUpmsUserId(String upmsUserId) {
        return null;
    }

    @Override
    public List<UpmsSystem> selectUpmsSystemByExample(UpmsSystemExample upmsSystemExample) {
        return null;
    }

    @Override
    public List<UpmsOrganization> selectUpmsOrganizationByExample(UpmsOrganizationExample upmsOrganizationExample) {
        return null;
    }

    @Override
    public UpmsUser selectUpmsUserByUsername(String username) {
        return null;
    }

    @Override
    public int insertUpmsLogSelective(UpmsLog record) {
        return 0;
    }

    @Override
    public List<Map> executeSelectSql(Map map) {
        return null;
    }

    @Override
    public Long executeUpdateSql(Map map) {
        return null;
    }

    @Override
    public Long executeDeleteSql(Map map) {
        return null;
    }

    @Override
    public UpmsUser selectUpmsUserByOpenId(String openId) {
        return null;
    }
}
