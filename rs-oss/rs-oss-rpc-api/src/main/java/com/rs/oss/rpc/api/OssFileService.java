package com.rs.oss.rpc.api;

import com.rs.common.base.RsBaseService;
import com.rs.oss.dao.model.OssFile;
import com.rs.oss.dao.model.OssFileExample;

/**
*
* OssFileService接口
* @author liegou
* @date 2018/7/16.
*/
public interface OssFileService extends RsBaseService<OssFile, OssFileExample> {
    String save(OssFile ossFile);

}