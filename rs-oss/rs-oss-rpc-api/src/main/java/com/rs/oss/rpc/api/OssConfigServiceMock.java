package com.rs.oss.rpc.api;

import com.rs.common.base.BaseServiceMock;
import com.rs.oss.dao.mapper.OssConfigMapper;
import com.rs.oss.dao.model.OssConfig;
import com.rs.oss.dao.model.OssConfigExample;

/**
*
* 降级实现OssConfigService接口
* @author liegou
* @date 2018/7/16.
*/
public class OssConfigServiceMock extends BaseServiceMock<OssConfigMapper, OssConfig, OssConfigExample> implements OssConfigService {

}
