package com.rs.oss.rpc.api;


import com.rs.oss.dao.model.OssConfig;

/**
 * oss系统接口
 * @author liegou
 */
public interface OssApiService {

    /**
     * 获取配置信息
     * 配置对象，此配置只存在一条数据,后续会扩展多对象管理
     * @return
     */
    OssConfig selectOssConfig();
}
