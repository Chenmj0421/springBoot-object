package com.rs.oss.rpc.api;

import com.rs.common.base.BaseServiceMock;
import com.rs.oss.dao.mapper.OssServerMapper;
import com.rs.oss.dao.model.OssServer;
import com.rs.oss.dao.model.OssServerExample;

/**
*
* 降级实现OssServerService接口
* @author liegou
* @date 2018/7/16.
*/
public class OssServerServiceMock extends BaseServiceMock<OssServerMapper, OssServer, OssServerExample> implements OssServerService {

}
