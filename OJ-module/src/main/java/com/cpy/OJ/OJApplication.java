package com.cpy.OJ;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/licpy">程序员鱼皮</a>
 * @from <a href="https://cpy.icu">编程导航知识星球</a>
 */
@SpringBootApplication
@MapperScan("com.cpy.OJ.mapper")
@EnableScheduling
@EnableRedisHttpSession//启用分布式session
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class OJApplication {

    public static void main(String[] args) {
        SpringApplication.run(OJApplication.class, args);
    }

}
