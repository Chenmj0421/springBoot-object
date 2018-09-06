package com.rs.upms.dao.mapper;

import com.rs.upms.dao.model.UpmsPermission;
import com.rs.upms.dao.model.UpmsPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UpmsPermissionMapper {
    long countByExample(UpmsPermissionExample example);

    int deleteByExample(UpmsPermissionExample example);

    int deleteByPrimaryKey(String permissionId);

    int insert(UpmsPermission record);

    int insertSelective(UpmsPermission record);

    List<UpmsPermission> selectByExample(UpmsPermissionExample example);

    UpmsPermission selectByPrimaryKey(String permissionId);

    int updateByExampleSelective(@Param("record") UpmsPermission record, @Param("example") UpmsPermissionExample example);

    int updateByExample(@Param("record") UpmsPermission record, @Param("example") UpmsPermissionExample example);

    int updateByPrimaryKeySelective(UpmsPermission record);

    int updateByPrimaryKey(UpmsPermission record);
}