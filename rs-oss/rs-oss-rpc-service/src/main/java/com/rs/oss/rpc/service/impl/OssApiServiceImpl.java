package com.rs.oss.rpc.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.rs.oss.dao.model.OssConfig;
import com.rs.oss.rpc.api.OssApiService;
import com.rs.oss.rpc.mapper.OssApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 程庆红
 * oss接口Service实现
 * @date 2018/1/08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OssApiServiceImpl implements OssApiService {

    @Autowired
    OssApiMapper ossApiMapper;

    @Override
    public OssConfig selectOssConfig() {
        return ossApiMapper.selectOssConfig();
    }
}
