package com.rs.upms.rpc.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.upms.dao.mapper.UpmsUserMapper;
import com.rs.upms.dao.model.UpmsUser;
import com.rs.upms.dao.model.UpmsUserExample;
import com.rs.upms.rpc.api.UpmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* UpmsUserService实现
* @author liegou
* @date 2018/6/20.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class UpmsUserServiceImpl extends BaseServiceImpl<UpmsUserMapper, UpmsUser, UpmsUserExample> implements UpmsUserService {

    private static Logger log = LoggerFactory.getLogger(UpmsUserServiceImpl.class);

    @Autowired
    UpmsUserMapper upmsUserMapper;

}