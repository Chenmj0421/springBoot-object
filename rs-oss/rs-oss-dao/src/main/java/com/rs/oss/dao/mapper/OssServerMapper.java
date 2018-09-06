package com.rs.oss.dao.mapper;

import com.rs.oss.dao.model.OssServer;
import com.rs.oss.dao.model.OssServerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OssServerMapper {
    long countByExample(OssServerExample example);

    int deleteByExample(OssServerExample example);

    int deleteByPrimaryKey(String id);

    int insert(OssServer record);

    int insertSelective(OssServer record);

    List<OssServer> selectByExample(OssServerExample example);

    OssServer selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OssServer record, @Param("example") OssServerExample example);

    int updateByExample(@Param("record") OssServer record, @Param("example") OssServerExample example);

    int updateByPrimaryKeySelective(OssServer record);

    int updateByPrimaryKey(OssServer record);
}