package com.rs.oss.rpc.mapper;


import com.rs.oss.dao.model.OssConfig;

/**
 * 用户VOMapper
 * @author liegou
 */
public interface OssApiMapper {
    /**
     * 获取配置信息
     * @return 配置对象，此配置只存在一条数据
     */
    OssConfig selectOssConfig();

}