package com.rs.oss.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.oss.dao.mapper.OssServerMapper;
import com.rs.oss.dao.model.OssServer;
import com.rs.oss.dao.model.OssServerExample;
import com.rs.oss.rpc.api.OssServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* OssServerService实现
* @author liegou
* @date 2018/7/16.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class OssServerServiceImpl extends BaseServiceImpl<OssServerMapper, OssServer, OssServerExample> implements OssServerService {

    private static Logger log = LoggerFactory.getLogger(OssServerServiceImpl.class);

    @Autowired
    OssServerMapper ossServerMapper;

}