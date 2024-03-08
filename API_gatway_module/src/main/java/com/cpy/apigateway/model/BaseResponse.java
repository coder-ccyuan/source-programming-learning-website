package com.cpy.apigateway.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 */
@Data
public class BaseResponse implements Serializable {
    private int code;
    private String data;
    private String message;
    private String description;


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
