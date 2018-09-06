package com.rs.oss.rpc.api;

import com.rs.common.base.BaseServiceMock;
import com.rs.oss.dao.mapper.OssFileMapper;
import com.rs.oss.dao.model.OssFile;
import com.rs.oss.dao.model.OssFileExample;

/**
*
* 降级实现OssFileService接口
* @author liegou
* @date 2018/7/16.
*/
public class OssFileServiceMock extends BaseServiceMock<OssFileMapper, OssFile, OssFileExample> implements OssFileService {

    @Override
    public String save(OssFile ossFile) {
        return null;
    }
}
