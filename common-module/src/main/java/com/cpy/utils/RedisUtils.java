package com.cpy.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.cpy.model.dto.redis.RedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.cpy.constant.RedisConstant.CACHE_NULL_TTL;
import static com.cpy.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @Author:成希德
 * redis 工具类
 */
@Component
@Slf4j
public class RedisUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR= Executors.newFixedThreadPool(10);

    /**
     *获取userId在redis中
     * @param token token
     * @return 用户Id
     */
    public  Long getUserIdFromRedis(String token){
         Long userId=Long.parseLong(stringRedisTemplate.opsForValue().get(USER_LOGIN_STATE + ":" + token)) ;
        return userId;
    }

    /**
     * 存储userId在redis中
     * @param userId
     * @param token
     */
    public void setUserIdToRedis(Long userId,String token){
        stringRedisTemplate.opsForValue().set(USER_LOGIN_STATE + ":" + token,userId.toString(),3600, TimeUnit.SECONDS);
    }
    /**
     * 存储缓存，使用opsForValue
     * @param key 缓存键
     * @param obj 缓存对象
     * @return
     */
    public<T> void setUseOpsForValue(String key,T obj){
        String json = JSONUtil.toJsonStr(obj);
        stringRedisTemplate.opsForValue().set(key,json);
    }

    /**
     *
     * @param key 缓存键
     * @param tClass 缓存对象的Class
     * @return 缓存对象
     * @param <T>
     */
    public<T> T getUseOpsForValue(String key,Class<T> tClass){
        String json = stringRedisTemplate.opsForValue().get(key);
        T t = JSONUtil.toBean(json, tClass);
        return t;
    }
    /**
     * 存储对象
     * @param key 缓存键
     * @param value 缓存对象
     * @param time 设定过期时间
     * @param unit 时间单位
     */
    public void set(String key,Object value,Long time,TimeUnit unit){
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(value),time,unit);
    }

    /**
     * 数据以逻辑过期形式写入redis
     * @param key 缓存键
     * @param value 缓存对象
     * @param time 设定过期时间
     * @param unit 时间单位
     */
    public void setWithLogicalExpire(String key,Object value,Long time,TimeUnit unit){
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(JSONUtil.toJsonStr(value));
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));
        //写入redis
        stringRedisTemplate.opsForValue().set(key,JSONUtil.toJsonStr(redisData));
    }

    /**
     *解决缓存穿透
     * @param keyPrefix 缓存键前缀
     * @param id 缓存id
     * @param type 返回类型
     * @param dbFallback 数据库调用方法
     * @param time 过期时间
     * @param unit 过期单位
     * @return
     * @param <R> 返回类型
     * @param <ID> 缓存id
     */
    public<R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type,
                                        Function<ID,R> dbFallback,Long time,TimeUnit unit){
        String key=keyPrefix+id;
        //1.从redis中查询
        String json = stringRedisTemplate.opsForValue().get(key);
        //2.判断是否存在
        if(StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json,type);
        }
        //判断命中是否时个空值 “”
        if(json!=null){
            return null;
        }
        //为null查询数据库
        R r = dbFallback.apply(id);
        if (r==null){
            //空值写入redis,解决缓存穿透
            set(key,"",CACHE_NULL_TTL,TimeUnit.DAYS);
            return null;
        }
        set(key,r,time,unit);
        return r;
    }

    /**
     *解决缓存击穿,使用逻辑过期方法
     * @param keyPrefix 缓存键前缀
     * @param id 缓存id
     * @param type 返回类型
     * @param dbFallback 数据库调用方法
     * @param time 过期时间
     * @param unit 过期单位
     * @param lockKeyPrefix 锁前缀
     * @return
     * @param <R> 返回类型
     * @param <ID> 缓存id
     */
    public<R,ID> R queryWithLogicalExpire(String keyPrefix, ID id, Class<R> type,
                                        Function<ID,R> dbFallback,Long time,TimeUnit unit,String lockKeyPrefix){
        String key=keyPrefix+id;
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(json)){
            //直接返回空，无需做缓存穿透处理，但要使数据库里的数据redis中都有
            return null;
        }
        RedisData redisData=BeanUtil.toBean(json, RedisData.class);
        String data = redisData.getData();
        R r=BeanUtil.toBean(data,type);
        //判断是否过期
        LocalDateTime expireTime = redisData.getExpireTime();
        if (expireTime.isAfter(LocalDateTime.now())){//没有逻辑过期
            return r;
        }
        //作为分布式锁的key
        String lockKey= lockKeyPrefix+id;
        boolean b = tyrLock(lockKey);
        if (b){
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try {
                    R newR = dbFallback.apply(id);
                    //重建缓存
                    setWithLogicalExpire(key,newR,time,unit);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    //释放锁
                    unlock(lockKey);
                }
            });
        }
        //返回过期数据
        return r;
    }

    /**
     * 尝试获取锁
     * @param key
     * @return
     */
    private boolean tyrLock(String key){
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return aBoolean;
    }

    /**
     * 释放锁
     * @param key
     */
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }
}
