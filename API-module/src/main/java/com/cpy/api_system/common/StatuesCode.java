package com.cpy.api_system.common;

/**
 *状态码信息
 */
public enum StatuesCode {
    SUCCESS(20000, "成功", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NO_AUTH(40100, "无权限", ""),
    NO_LOGIN(40101, "未登录", ""),
    SYSTEM_EXCEPTION(50000, "系统异常", "");
    private final int code;
    private final String message;
    private final String description;
    StatuesCode(int code,String message,String description){
        this.code=code;
        this.message=message;
        this.description=description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
