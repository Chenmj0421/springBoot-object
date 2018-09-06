package com.rs.oss.dao.mapper;

import com.rs.oss.dao.model.OssFile;
import com.rs.oss.dao.model.OssFileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OssFileMapper {
    long countByExample(OssFileExample example);

    int deleteByExample(OssFileExample example);

    int deleteByPrimaryKey(String id);

    int insert(OssFile record);

    int insertSelective(OssFile record);

    List<OssFile> selectByExample(OssFileExample example);

    OssFile selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OssFile record, @Param("example") OssFileExample example);

    int updateByExample(@Param("record") OssFile record, @Param("example") OssFileExample example);

    int updateByPrimaryKeySelective(OssFile record);

    int updateByPrimaryKey(OssFile record);
}