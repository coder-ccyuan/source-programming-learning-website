package com.yupi.springbootinit.utils;

import com.cpy.OJ.OJApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @Author:成希德
 */
@SpringBootTest(classes = OJApplication.class)
public class RedisUtile {
    @Resource
    RedisTemplate redisTemplate;
    @Test
    void test(){
        System.out.println(redisTemplate);
    }
}
