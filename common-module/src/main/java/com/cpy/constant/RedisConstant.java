package com.cpy.constant;

/**
 * redis 相关常量
 */
public interface RedisConstant {
    /**
     * 空对象存储时间（解决缓存穿透）
     */
    Long CACHE_NULL_TTL=1L;
}
