package com.rs.oss.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.oss.dao.mapper.OssFileMapper;
import com.rs.oss.dao.model.OssFile;
import com.rs.oss.dao.model.OssFileExample;
import com.rs.oss.dao.model.OssServer;
import com.rs.oss.rpc.api.OssFileService;
import com.rs.oss.rpc.api.OssServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* OssFileService实现
* @author liegou
* @date 2018/7/16.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class OssFileServiceImpl extends BaseServiceImpl<OssFileMapper, OssFile, OssFileExample> implements OssFileService {

    private static Logger log = LoggerFactory.getLogger(OssFileServiceImpl.class);

    @Autowired
    OssFileMapper ossFileMapper;
    @Autowired
    OssServerService ossServerService;
    @Override
    public String save(OssFile ossFile){
        ossFileMapper.insertSelective(ossFile);
        return ossFile.getId();

    }

}