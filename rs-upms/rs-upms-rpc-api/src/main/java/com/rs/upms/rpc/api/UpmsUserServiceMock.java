package com.rs.upms.rpc.api;

import com.rs.common.base.BaseServiceMock;
import com.rs.upms.dao.mapper.UpmsUserMapper;
import com.rs.upms.dao.model.UpmsUser;
import com.rs.upms.dao.model.UpmsUserExample;

/**
*
* 降级实现UpmsUserService接口
* @author liegou
* @date 2018/6/20.
*/
public class UpmsUserServiceMock extends BaseServiceMock<UpmsUserMapper, UpmsUser, UpmsUserExample> implements UpmsUserService {

}
