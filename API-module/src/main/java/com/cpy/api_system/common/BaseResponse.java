package com.cpy.api_system.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>返回数据
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(T data, StatuesCode statuesCode) {
        this.data = data;
        this.code = statuesCode.getCode();
        this.message = statuesCode.getMessage();
        this.description = statuesCode.getDescription();
    }

    public BaseResponse(StatuesCode statuesCode) {
        this.code = statuesCode.getCode();
        this.message = statuesCode.getMessage();
        this.description = statuesCode.getDescription();
    }
    public BaseResponse(StatuesCode statuesCode,String description) {
        this.code = statuesCode.getCode();
        this.message = statuesCode.getMessage();
        this.description = description;
    }

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public BaseResponse() {
    }
}
