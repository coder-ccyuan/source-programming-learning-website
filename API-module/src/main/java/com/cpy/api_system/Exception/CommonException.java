package com.cpy.api_system.Exception;

import com.cpy.api_system.common.StatuesCode;

/**
 * 通用异常类
 */
public class CommonException extends RuntimeException{
    private int code;
    private String description;
    private String message;
    public CommonException(StatuesCode statuesCode,String description){
        super(statuesCode.getMessage());
        this.code=statuesCode.getCode();
        this.message=statuesCode.getMessage();
        this.description=description;
    }
    public CommonException(StatuesCode statuesCode){
        super(statuesCode.getMessage());
        this.code=statuesCode.getCode();
        this.message=statuesCode.getMessage();
    }
    public CommonException(int code, String description,String message){
        super(message);
        this.code=code;
        this.message=message;
        this.description=description;
    }
    public CommonException(int code,String message){
        super(message);
        this.code=code;
        this.message=message;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
