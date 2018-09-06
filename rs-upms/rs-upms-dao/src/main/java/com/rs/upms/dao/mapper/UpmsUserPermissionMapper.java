package com.rs.upms.dao.mapper;

import com.rs.upms.dao.model.UpmsUserPermission;
import com.rs.upms.dao.model.UpmsUserPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UpmsUserPermissionMapper {
    long countByExample(UpmsUserPermissionExample example);

    int deleteByExample(UpmsUserPermissionExample example);

    int deleteByPrimaryKey(String userPermissionId);

    int insert(UpmsUserPermission record);

    int insertSelective(UpmsUserPermission record);

    List<UpmsUserPermission> selectByExample(UpmsUserPermissionExample example);

    UpmsUserPermission selectByPrimaryKey(String userPermissionId);

    int updateByExampleSelective(@Param("record") UpmsUserPermission record, @Param("example") UpmsUserPermissionExample example);

    int updateByExample(@Param("record") UpmsUserPermission record, @Param("example") UpmsUserPermissionExample example);

    int updateByPrimaryKeySelective(UpmsUserPermission record);

    int updateByPrimaryKey(UpmsUserPermission record);
}