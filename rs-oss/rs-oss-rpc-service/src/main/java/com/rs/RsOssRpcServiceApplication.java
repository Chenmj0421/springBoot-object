package com.rs;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * 启动类
 *
 * @author liegou
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
@ImportResource(locations = {"META-INF/spring/applicationContext.xml"})
@MapperScan({"com.rs.oss"})
@ComponentScan(basePackages={"com.rs.common","com.codingapi.tx"})
public class RsOssRpcServiceApplication {
    private static Logger log = LoggerFactory.getLogger(RsOssRpcServiceApplication.class);

    public static void main(String[] args) {
        log.info(">>>>> rs-oss-rpc-service 正在启动 <<<<<");
        SpringApplication app = new SpringApplication(RsOssRpcServiceApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
        log.info(">>>>> rs-oss-rpc-service 启动完成 <<<<<");
        // 在服务器存在多个网卡时，可开启此处以测试暴露的ip是否正确
        // getLocalip();
    }

    private static void getLocalip() {
        try {
            log.info("服务暴露的ip: "
                    + java.net.InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
