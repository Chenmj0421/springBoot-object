package com.rs.oss.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.oss.dao.mapper.OssConfigMapper;
import com.rs.oss.dao.model.OssConfig;
import com.rs.oss.dao.model.OssConfigExample;
import com.rs.oss.rpc.api.OssConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* OssConfigService实现
* @author liegou
* @date 2018/7/16.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class OssConfigServiceImpl extends BaseServiceImpl<OssConfigMapper, OssConfig, OssConfigExample> implements OssConfigService {

    private static Logger log = LoggerFactory.getLogger(OssConfigServiceImpl.class);

    @Autowired
    OssConfigMapper ossConfigMapper;

}