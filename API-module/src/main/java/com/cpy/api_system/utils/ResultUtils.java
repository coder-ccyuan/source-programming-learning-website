package com.cpy.api_system.utils;

import com.cpy.api_system.common.BaseResponse;
import com.cpy.api_system.common.StatuesCode;

/**
 * 返回结果工具类
 */
public class ResultUtils {
    /**
     *
     * @param data 返回数据
     * @return 通用返回类
     */
    public static <T>BaseResponse<T> success(T data){
        return new BaseResponse<T>(data,StatuesCode.SUCCESS);
    }

    /**
     * @param statuesCode 返回状态信息
     * @return 通用返回类
     */
    public static BaseResponse error(StatuesCode statuesCode){
        return new BaseResponse(statuesCode);
    }

    /**
     *
     * @param statuesCode 返回状态信息
     * @param description 详细信息
     * @return 通用返回类
     */
    public static BaseResponse error(StatuesCode statuesCode,String description){
        return new BaseResponse(statuesCode,description);
    }

    /**
     * @param code
     * @param description
     * @param message
     * @return
     */
    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse(code,message,description);
    }

    /**
     * @param code
     * @param message
     * @return
     */
    public static BaseResponse error(int code,String message){
        return new BaseResponse(code,message);
    }
}
