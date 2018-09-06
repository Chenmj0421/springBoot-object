package com.rs.oss.rpc.api;


import com.rs.oss.dao.model.OssConfig;

/**
 * @author liegou
 * 降级实现ossApiService接口
 */
public class OssApiServiceMock implements OssApiService{
    @Override
    public OssConfig selectOssConfig() {
        return null;
    }
}
