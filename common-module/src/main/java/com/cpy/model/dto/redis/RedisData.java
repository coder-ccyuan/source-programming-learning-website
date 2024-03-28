package com.cpy.model.dto.redis;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * redis 数据封装类
 * @Author:成希德
 */
@Data
public class RedisData {
    private String Data;
    private LocalDateTime expireTime;
}
