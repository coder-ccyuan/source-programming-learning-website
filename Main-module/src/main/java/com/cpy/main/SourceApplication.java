package com.cpy.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/licpy">程序员鱼皮</a>
 * @from <a href="https://cpy.icu">编程导航知识星球</a>
 */
@SpringBootApplication
@ComponentScan(value = {"com.cpy.config","com.cpy.main","com.cpy.utils"})
@MapperScan("com.cpy.main.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class SourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceApplication.class, args);
    }

}
