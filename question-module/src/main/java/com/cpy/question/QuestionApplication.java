package com.cpy.question;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDiscoveryClient
@EnableFeignClients
@EnableKnife4j
@EnableRedisHttpSession
@SpringBootApplication
@MapperScan("com.cpy.question.mapper")
@ComponentScan("com.cpy")
public class QuestionApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuestionApplication.class,args);
    }
}