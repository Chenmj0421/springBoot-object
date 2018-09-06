package com.rs.upms.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.upms.dao.mapper.UpmsUserRoleMapper;
import com.rs.upms.dao.model.UpmsUserRole;
import com.rs.upms.dao.model.UpmsUserRoleExample;
import com.rs.upms.rpc.api.UpmsUserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* UpmsUserRoleService实现
* @author liegou
* @date 2018/6/20.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class UpmsUserRoleServiceImpl extends BaseServiceImpl<UpmsUserRoleMapper, UpmsUserRole, UpmsUserRoleExample> implements UpmsUserRoleService {

    private static Logger log = LoggerFactory.getLogger(UpmsUserRoleServiceImpl.class);

    @Autowired
    UpmsUserRoleMapper upmsUserRoleMapper;

}