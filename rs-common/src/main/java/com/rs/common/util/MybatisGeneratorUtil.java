package com.rs.common.util;

import org.apache.commons.lang.ObjectUtils;
import org.apache.velocity.VelocityContext;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.rs.common.util.StringUtil.lineToHump;

/**
 * 代码自动生成类
 * @author liegou
 * @date
 */
public class MybatisGeneratorUtil {

	/** generatorConfig模板路径*/
	private static String generatorConfig_vm = "/template/generatorConfig.vm";
	/** Service模板路径*/
	private static String service_vm = "/template/Service.vm";
	 /** ServiceMock模板路径*/
	private static String serviceMock_vm = "/template/ServiceMock.vm";
	 /** ServiceImpl模板路径*/
	private static String serviceImpl_vm = "/template/ServiceImpl.vm";

	 /** controller模板路径*/
	private static String controller_vm = "/template/controller.vm";
	 /** sql模板路径*/
	private static String sql_vm = "/template/sql.vm";
	/**
	 * 根据模板生成generatorConfig.xml文件
	 * @param jdbc_driver   驱动路径
	 * @param jdbc_url      链接
	 * @param jdbc_username 帐号
	 * @param jdbc_password 密码
	 * @param module        项目模块
	 * @param database      数据库
	 * @param table_prefix  表前缀
	 * @param package_name  包名
	 */
	public static void generator(
			String jdbc_driver,
			String jdbc_url,
			String jdbc_username,
			String jdbc_password,
			String module,
			String database,
			String table_prefix,
			String package_name,
			Map<String, String> last_insert_id_tables, String project_name) throws Exception{
		String os = System.getProperty("os.name");
		String targetProject = module + "/" + module + "-dao";
		String basePath = MybatisGeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "").replace(targetProject, "").replaceFirst("/", "");
		if (os.toLowerCase().startsWith("win")) {
			System.out.println("========== win ==========");
			generatorConfig_vm = MybatisGeneratorUtil.class.getResource(generatorConfig_vm).getPath().replaceFirst("/", "");
			service_vm = MybatisGeneratorUtil.class.getResource(service_vm).getPath().replaceFirst("/", "");
			serviceMock_vm = MybatisGeneratorUtil.class.getResource(serviceMock_vm).getPath().replaceFirst("/", "");
			serviceImpl_vm = MybatisGeneratorUtil.class.getResource(serviceImpl_vm).getPath().replaceFirst("/", "");
			//basePath = basePath.replaceFirst("/", "");
		} else {
			System.out.println("========== linux ==========");
			generatorConfig_vm = MybatisGeneratorUtil.class.getResource(generatorConfig_vm).getPath();
			service_vm = MybatisGeneratorUtil.class.getResource(service_vm).getPath();
			serviceMock_vm = MybatisGeneratorUtil.class.getResource(serviceMock_vm).getPath();
			serviceImpl_vm = MybatisGeneratorUtil.class.getResource(serviceImpl_vm).getPath();
		}
		String generatorConfig_xml = MybatisGeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "") + "/src/main/resources/generatorConfig.xml";
		targetProject = basePath + targetProject;
		String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + database + "' AND table_name LIKE '" + table_prefix + "_%';";
        String columnSql = "SELECT table_name,COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '" + database + "' AND table_name like '" + table_prefix + "_%'  AND COLUMN_KEY ='PRI';";
		String ctime = new SimpleDateFormat("yyyy/M/d").format(new Date());
		String servicePath = basePath + module + "/" + module + "-rpc-api" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/rpc/api";
		String serviceImplPath = basePath + module + "/" + module + "-rpc-service" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/rpc/service/impl";
		String controllerPath = basePath + module + "/" + module + "-admin" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/admin/controller/manage";
		String sqlPath = basePath + module + "/" + module + "-admin"  + "/sql";
		String modelPath=targetProject + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/model";
		String mapperPath = targetProject + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/mapper";
		String sqlMapperPath = basePath + module + "/" + module + "-rpc-service" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/mapper";
		System.out.println("========== 开始生成文件目录 ==========");
		checkFilePath(servicePath);
		checkFilePath(serviceImplPath);
		checkFilePath(controllerPath);
		checkFilePath(sqlPath);
		checkFilePath(modelPath);
		checkFilePath(mapperPath);
		checkFilePath(sqlMapperPath);
		System.out.println("========== 结束生成文件目录 ==========");
        System.out.println("========== 开始生成generatorConfig.xml文件 ==========");
		List<Map<String, Object>> tables = new ArrayList<>();
		try {
			VelocityContext context = new VelocityContext();
			Map<String, Object> table;
			// 查询定制前缀项目的所有表
			JdbcUtil jdbcUtil = new JdbcUtil(jdbc_driver, jdbc_url, jdbc_username, AESUtil.AESDecode(jdbc_password));
			List<Map> result = jdbcUtil.selectByParams(sql, null);
			List<Map> columnResult = jdbcUtil.selectByParams(columnSql, null);
			for (Map map : result) {
				System.out.println(map.get("TABLE_NAME"));
				table = new HashMap<>(16);
				table.put("table_name", map.get("TABLE_NAME"));
				table.put("model_name", lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
				tables.add(table);
			}
			// 获取主键列
			for (Map map :columnResult) {
				last_insert_id_tables.put((String)map.get("TABLE_NAME"),(String)map.get("COLUMN_NAME"));
			}
			jdbcUtil.release();

			String targetProject_sqlMap = basePath + module + "/" + module + "-rpc-service";
			context.put("tables", tables);
			context.put("generator_javaModelGenerator_targetPackage", package_name + ".dao.model");
			context.put("generator_sqlMapGenerator_targetPackage", package_name + ".dao.mapper");
			context.put("generator_javaClientGenerator_targetPackage", package_name + ".dao.mapper");
			context.put("targetProject", targetProject);
			context.put("targetProject_sqlMap", targetProject_sqlMap);
			context.put("generator_jdbc_password", AESUtil.AESDecode(jdbc_password));
			context.put("last_insert_id_tables", last_insert_id_tables);
			VelocityUtil.generate(generatorConfig_vm, generatorConfig_xml, context);
			// 删除旧代码
			deleteDir(new File(targetProject + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/model"));
			deleteDir(new File(targetProject + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/mapper"));
			deleteDir(new File(targetProject_sqlMap + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/mapper"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("========== 结束生成generatorConfig.xml文件 ==========");

		System.out.println("========== 开始运行MybatisGenerator ==========");
		List<String> warnings = new ArrayList<>();
		File configFile = new File(generatorConfig_xml);
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(true);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		for (String warning : warnings) {
			System.out.println(warning);
		}
		System.out.println("========== 结束运行MybatisGenerator ==========");

		System.out.println("========== 开始生成Service ==========");
		for (int i = 0; i < tables.size(); i++) {
			String model = StringUtil.lineToHump(ObjectUtils.toString(tables.get(i).get("table_name")));
			String service = servicePath + "/" + model + "Service.java";
			String serviceMock = servicePath + "/" + model + "ServiceMock.java";
			String serviceImpl = serviceImplPath + "/" + model + "ServiceImpl.java";
			String controller = controllerPath + "/" + model + "Controller.java";
			String sqlF = sqlPath + "/" + model + "menu.sql";
			// 生成service
			File serviceFile = new File(service);
			if (!serviceFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("ctime", ctime);
				VelocityUtil.generate(service_vm, service, context);
				System.out.println(service);
			}
			// 生成serviceMock
			File serviceMockFile = new File(serviceMock);
			if (!serviceMockFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("ctime", ctime);
				VelocityUtil.generate(serviceMock_vm, serviceMock, context);
				System.out.println(serviceMock);
			}
			// 生成serviceImpl
			File serviceImplFile = new File(serviceImpl);
			if (!serviceImplFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
				context.put("ctime", ctime);
				VelocityUtil.generate(serviceImpl_vm, serviceImpl, context);
			}
			System.out.println("========== 结束生成Service ==========");
			System.out.println("========== 开始生成Controller ==========");
			File controllerFile = new File(controller);
			if (!controllerFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
				context.put("permission", ObjectUtils.toString(tables.get(i).get("table_name")).replaceAll("_",":"));
				context.put("project_name", project_name);
				context.put("project", StringUtil.toLowerCaseFirstOne(project_name));
				context.put("ctime", ctime);
				VelocityUtil.generate(controller_vm, controller, context);
			}
			System.out.println("========== 结束生成Controller ==========");
			// 生成菜单sql语句
			File sqlFile = new File(sqlF);
			if (!sqlFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
				context.put("permission", ObjectUtils.toString(tables.get(i).get("table_name")).replaceAll("_",":"));
				context.put("project_name", project_name);
				context.put("project", StringUtil.toLowerCaseFirstOne(project_name));
				context.put("ctime", ctime);
				VelocityUtil.generate(sql_vm, sqlF, context);
			}
		}

	}
	public static boolean checkFilePath(String filePath){
		String [] paths=filePath.split("/");
		StringBuffer fullPath=new StringBuffer();
		for (int i = 0; i < paths.length; i++) {
			fullPath.append(paths[i]).append("\\\\");
			File file=new File(fullPath.toString());

			if(!file.exists()){
				file.mkdir();
			}

		}
		File file=new File(fullPath.toString());
		if (!file.exists()) {
			return false;
		}else{
			return true;
		}

	}
	public static void generator(
			String jdbc_driver,
			String jdbc_url,
			String jdbc_username,
			String jdbc_password,
			String module,
			String database,
			String table_prefix,
			String package_name,
			Map<String, String> last_insert_id_tables) throws Exception{

		generatorConfig_vm = MybatisGeneratorUtil.class.getResource(generatorConfig_vm).getPath().replaceFirst("/", "");
		service_vm = MybatisGeneratorUtil.class.getResource(service_vm).getPath().replaceFirst("/", "");
		serviceMock_vm = MybatisGeneratorUtil.class.getResource(serviceMock_vm).getPath().replaceFirst("/", "");
		serviceImpl_vm = MybatisGeneratorUtil.class.getResource(serviceImpl_vm).getPath().replaceFirst("/", "");

		String targetProject = module + "/" + module + "-dao";
		String basePath = MybatisGeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "").replace(targetProject, "").replaceFirst("/", "");
		String generatorConfig_xml = MybatisGeneratorUtil.class.getResource("/").getPath().replace("/target/classes/", "") + "/src/main/resources/generatorConfig.xml";
		targetProject = basePath + targetProject;
		String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + database + "' AND table_name LIKE '" + table_prefix + "_%';";
		String columnSql = "SELECT table_name,COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '" + database + "' AND table_name like '" + table_prefix + "_%'  AND COLUMN_KEY ='PRI';";
		System.out.println("========== 开始生成generatorConfig.xml文件 ==========");
		List<Map<String, Object>> tables = new ArrayList<>();
		try {
			VelocityContext context = new VelocityContext();
			Map<String, Object> table;
			// 查询定制前缀项目的所有表
			JdbcUtil jdbcUtil = new JdbcUtil(jdbc_driver, jdbc_url, jdbc_username, AESUtil.AESDecode(jdbc_password));
			List<Map> result = jdbcUtil.selectByParams(sql, null);
			List<Map> columnResult = jdbcUtil.selectByParams(columnSql, null);
			for (Map map : result) {
				System.out.println(map.get("TABLE_NAME"));
				table = new HashMap<>(16);
				table.put("table_name", map.get("TABLE_NAME"));
				table.put("model_name", lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
				tables.add(table);
			}
			for (Map map :columnResult) {
				last_insert_id_tables.put((String)map.get("TABLE_NAME"),(String)map.get("COLUMN_NAME"));
			}
			jdbcUtil.release();

			String targetProject_sqlMap = basePath + module + "/" + module + "-rpc-service";
			context.put("tables", tables);
			context.put("generator_javaModelGenerator_targetPackage", package_name + ".dao.model");
			context.put("generator_sqlMapGenerator_targetPackage", package_name + ".dao.mapper");
			context.put("generator_javaClientGenerator_targetPackage", package_name + ".dao.mapper");
			context.put("targetProject", targetProject);
			context.put("targetProject_sqlMap", targetProject_sqlMap);
			context.put("generator_jdbc_password", AESUtil.AESDecode(jdbc_password));
			context.put("last_insert_id_tables", last_insert_id_tables);
			VelocityUtil.generate(generatorConfig_vm, generatorConfig_xml, context);
			// 删除旧代码
			deleteDir(new File(targetProject + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/model"));
			deleteDir(new File(targetProject + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/mapper"));
			deleteDir(new File(targetProject_sqlMap + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/dao/mapper"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("========== 结束生成generatorConfig.xml文件 ==========");

		System.out.println("========== 开始运行MybatisGenerator ==========");
		List<String> warnings = new ArrayList<>();
		File configFile = new File(generatorConfig_xml);
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(true);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
		for (String warning : warnings) {
			System.out.println(warning);
		}
		System.out.println("========== 结束运行MybatisGenerator ==========");

		System.out.println("========== 开始生成Service ==========");
		String ctime = new SimpleDateFormat("yyyy/M/d").format(new Date());
		String servicePath = basePath + module + "/" + module + "-rpc-api" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/rpc/api";
		String serviceImplPath = basePath + module + "/" + module + "-rpc-service" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/rpc/service/impl";
		String controllerPath = basePath + module + "/" + module + "-admin" + "/src/main/java/" + package_name.replaceAll("\\.", "/") + "/admin/controller/manage";
		String sqlPath = basePath + module + "/" + module + "-admin"  + "/sql";
		for (int i = 0; i < tables.size(); i++) {
			String model = StringUtil.lineToHump(ObjectUtils.toString(tables.get(i).get("table_name")));
			String service = servicePath + "/" + model + "Service.java";
			String serviceMock = servicePath + "/" + model + "ServiceMock.java";
			String serviceImpl = serviceImplPath + "/" + model + "ServiceImpl.java";
			String controller = controllerPath + "/" + model + "Controller.java";
			String sqlF = sqlPath + "/" + model + "menu.sql";
			// 生成service
			File serviceFile = new File(service);
			if (!serviceFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("ctime", ctime);
				VelocityUtil.generate(service_vm, service, context);
				System.out.println(service);
			}
			// 生成serviceMock
			File serviceMockFile = new File(serviceMock);
			if (!serviceMockFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("ctime", ctime);
				VelocityUtil.generate(serviceMock_vm, serviceMock, context);
				System.out.println(serviceMock);
			}
			// 生成serviceImpl
			File serviceImplFile = new File(serviceImpl);
			if (!serviceImplFile.exists()) {
				VelocityContext context = new VelocityContext();
				context.put("package_name", package_name);
				context.put("model", model);
				context.put("mapper", StringUtil.toLowerCaseFirstOne(model));
				context.put("ctime", ctime);
				VelocityUtil.generate(serviceImpl_vm, serviceImpl, context);
			}

		}
		System.out.println("========== 结束生成Service ==========");

		System.out.println("========== 开始生成Controller ==========");
		System.out.println("========== 结束生成Controller ==========");
	}
	/** 递归删除非空文件夹*/
	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteDir(files[i]);
			}
		}
		dir.delete();
	}

}
