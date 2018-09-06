package com.rs.oss.dao.mapper;

import com.rs.oss.dao.model.OssConfig;
import com.rs.oss.dao.model.OssConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OssConfigMapper {
    long countByExample(OssConfigExample example);

    int deleteByExample(OssConfigExample example);

    int deleteByPrimaryKey(String id);

    int insert(OssConfig record);

    int insertSelective(OssConfig record);

    List<OssConfig> selectByExample(OssConfigExample example);

    OssConfig selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OssConfig record, @Param("example") OssConfigExample example);

    int updateByExample(@Param("record") OssConfig record, @Param("example") OssConfigExample example);

    int updateByPrimaryKeySelective(OssConfig record);

    int updateByPrimaryKey(OssConfig record);
}