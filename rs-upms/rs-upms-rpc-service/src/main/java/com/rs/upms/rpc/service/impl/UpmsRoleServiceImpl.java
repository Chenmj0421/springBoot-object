package com.rs.upms.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.upms.dao.mapper.UpmsRoleMapper;
import com.rs.upms.dao.model.UpmsRole;
import com.rs.upms.dao.model.UpmsRoleExample;
import com.rs.upms.rpc.api.UpmsRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* UpmsRoleService实现
* @author liegou
* @date 2018/6/20.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class UpmsRoleServiceImpl extends BaseServiceImpl<UpmsRoleMapper, UpmsRole, UpmsRoleExample> implements UpmsRoleService {

    private static Logger log = LoggerFactory.getLogger(UpmsRoleServiceImpl.class);

    @Autowired
    UpmsRoleMapper upmsRoleMapper;

}