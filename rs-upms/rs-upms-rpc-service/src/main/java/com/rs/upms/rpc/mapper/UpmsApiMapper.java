package com.rs.upms.rpc.mapper;


import com.rs.upms.dao.model.UpmsOrganization;
import com.rs.upms.dao.model.UpmsPermission;
import com.rs.upms.dao.model.UpmsRole;

import java.util.List;
import java.util.Map;

/**
 * 用户VOMapper
 * @author liegou
 */
public interface UpmsApiMapper {
	/**
	 * 根据用户id获取所拥有的权限
	 * @param upmsUserId 用户id
	 * @return List<UpmsPermission>
	 */
	List<UpmsPermission> selectUpmsPermissionByUpmsUserId(String upmsUserId);

	/**
	 * 根据用户id获取所属的角色
	 * @param upmsUserId 用户id
	 * @return UpmsRole
	 */
	List<UpmsRole> selectUpmsRoleByUpmsUserId(String upmsUserId);

	/**
	 * 根据用户Id 获取组织
	 * @param userId 用户ID
	 * @return UpmsOrganization
	 */
   //根据用户Id 获取组织
	List<UpmsOrganization> selectByOrganizationId(String userId);

	/**
	 * 执行select 语句
	 * @param map
	 * @return
	 */
	List<Map> executeSelectSql(Map map);

	/**
	 * 执行更新语句
	 * @param map
	 * @return
	 */
	Long  executeUpdateSql(Map map);

	/**
	 * 执行删除语句
	 * @param map
	 * @return
	 */
	Long  executeDeleteSql(Map map);
	
}