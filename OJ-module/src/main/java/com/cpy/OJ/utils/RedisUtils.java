package com.cpy.OJ.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.cpy.OJ.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @Author:成希德
 * redis 工具类
 */
@Component
public class RedisUtils {
    @Resource
     RedisTemplate<String,Object> redisTemplate;

    /**
     *获取userId在redis中
     * @param token token
     * @return 用户Id
     */
    public  Long getUserIdFromRedis(String token){
         Long userId=(Long) redisTemplate.opsForValue().get(USER_LOGIN_STATE + ":" + token);
        return userId;
    }

    /**
     * 存储userId在redis中
     * @param userId
     * @param token
     */
    public void setUserIdToRedis(Long userId,String token){
        redisTemplate.opsForValue().set(USER_LOGIN_STATE + ":" + token,userId,3600, TimeUnit.SECONDS);
    }
}
