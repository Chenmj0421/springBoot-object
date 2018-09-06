package com.rs.upms.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.upms.dao.mapper.UpmsLogMapper;
import com.rs.upms.dao.model.UpmsLog;
import com.rs.upms.dao.model.UpmsLogExample;
import com.rs.upms.rpc.api.UpmsLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* UpmsLogService实现
* @author liegou
* @date 2018/6/20.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class UpmsLogServiceImpl extends BaseServiceImpl<UpmsLogMapper, UpmsLog, UpmsLogExample> implements UpmsLogService {

    private static Logger log = LoggerFactory.getLogger(UpmsLogServiceImpl.class);

    @Autowired
    UpmsLogMapper upmsLogMapper;

}