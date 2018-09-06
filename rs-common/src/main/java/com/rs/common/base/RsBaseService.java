package com.rs.common.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BaseService接口
 * @author  liegou
 */
public interface RsBaseService<Record, Example> {
	/**
	 * 按条件查询总数
	 * @param example
	 * @return
	 */
	int countByExample(Example example);

	/**
	 * 根据条件删除数据
	 * @param example
	 * @return
	 */

	int deleteByExample(Example example);

	/**
	 * 根据主键删除数据
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(String id);

	/**
	 * 根据实体类插入数据
	 * @param record
	 * @return
	 */
	int insert(Record record);

	/**
	 * 实体类中有值得才插入
	 * @param record
	 * @return
	 */
	int insertSelective(Record record);

	/**
	 * 需检索的字段中包含大字段类型时,查询list
	 * @param example
	 * @return
	 */
	List<Record> selectByExampleWithBLOBs(Example example);

	/**
	 * 查询全量list
	 * @param example
	 * @return
	 */
	List<Record> selectByExample(Example example);

	/**
	 * 需检索的字段中包含大字段类型时,分页查询
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Record> selectByExampleWithBLOBsForStartPage(Example example, Integer pageNum, Integer pageSize);

	/**'
	 * 需检索的字段中不包含大字段类型时，分页查询
	 * @param example
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<Record> selectByExampleForStartPage(Example example, Integer pageNum, Integer pageSize);

	/**
	 * 需检索的字段中包含大字段类型时,分页查询
	 * @param example
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Record> selectByExampleWithBLOBsForOffsetPage(Example example, Integer offset, Integer limit);

	/**
	 * 需检索的字段中不包含大字段类型时,分页查询
	 * @param example
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Record> selectByExampleForOffsetPage(Example example, Integer offset, Integer limit);

	/**
	 * 查询结果中的第一条数据
	 * @param example
	 * @return
	 */
	Record selectFirstByExample(Example example);

	/**
	 * 需检索的字段中包含大字段类型时,查询结果中的第一条数据
	 * @param example
	 * @return
	 */
	Record selectFirstByExampleWithBLOBs(Example example);

	/**
	 * 根据主键查询
	 * @param id
	 * @return
	 */
	Record selectByPrimaryKey(String id);

	/**
	 * 根据有值得列进行更新
	 * @param record
	 * @param example
	 * @return
	 */
	int updateByExampleSelective(@Param("record") Record record, @Param("example") Example example);

	/**
	 * 包含大字段类型时的更新
	 * @param record
	 * @param example
	 * @return
	 */
	int updateByExampleWithBLOBs(@Param("record") Record record, @Param("example") Example example);

	/**
	 * 不包含大字段类型时的更新
	 * @param record
	 * @param example
	 * @return
	 */
	int updateByExample(@Param("record") Record record, @Param("example") Example example);

	/**
	 * 根据主键更新有值得列
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(Record record);

	/**
	 * 存在大字段类型时，根据主键进行更新
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeyWithBLOBs(Record record);

	/**
	 * 根据主键更新
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(Record record);

	/**
	 * 根据主键批量删除数据
	 * @param ids
	 * @return
	 */
	int deleteByPrimaryKeys(String ids);

	/**
	 * 初始化
	 */
    void initMapper();

}