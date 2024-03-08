package com.cpy.OJ;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 主类（项目启动入口）
 *
 */
@SpringBootApplication
@MapperScan("com.cpy.OJ.mapper")
@EnableScheduling
@EnableRedisHttpSession//启用分布式session
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan(basePackages = {"com.cpy.clientApi","com.cpy.config"})
public class OJApplication {

    public static void main(String[] args) {
        SpringApplication.run(OJApplication.class, args);
    }

}
