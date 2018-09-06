package com.rs.check.dao;

import com.rs.common.util.MybatisGeneratorUtil;
import com.rs.common.util.PropertiesFileUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成类
 * @author liegou
 */
public class Generator {

	/** 根据命名规范，只修改此常量值即可*/
	/** 系统名称*/
	private static String MODULE = "rs-check";
	/** 数据库名称*/
	private static String DATABASE = "rs";
	/** 表开头*/
	private static String TABLE_PREFIX = "check_";
	/** 系统名称 第一个字母大写*/
	private static String PROJECT_NAME = "Check";
	/** 包名*/
	private static String PACKAGE_NAME = "com.rs.check";
	private static String JDBC_DRIVER = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.driver");
	private static String JDBC_URL = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.url");
	private static String JDBC_USERNAME = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.username");
	private static String JDBC_PASSWORD = PropertiesFileUtil.getInstance("generator").get("generator.jdbc.password");
	// 需要insert后返回主键的表配置，key:表名,value:主键名
	private static Map<String, String> LAST_INSERT_ID_TABLES = new HashMap<>();
	static {

	}

	/**
	 * 自动代码生成
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		MybatisGeneratorUtil.generator(JDBC_DRIVER, JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD, MODULE, DATABASE, TABLE_PREFIX, PACKAGE_NAME, LAST_INSERT_ID_TABLES, PROJECT_NAME);
	}


}
