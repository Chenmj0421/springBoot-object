package com.rs.upms.rpc.api;

import com.rs.common.base.BaseServiceMock;
import com.rs.upms.dao.mapper.UpmsLogMapper;
import com.rs.upms.dao.model.UpmsLog;
import com.rs.upms.dao.model.UpmsLogExample;

/**
*
* 降级实现UpmsLogService接口
* @author liegou
* @date 2018/6/20.
*/
public class UpmsLogServiceMock extends BaseServiceMock<UpmsLogMapper, UpmsLog, UpmsLogExample> implements UpmsLogService {

}
