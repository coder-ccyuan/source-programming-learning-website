package com.cpy.api_system.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInvokeTestRequest implements Serializable {
    Long id;
    /**
     * 请求对象的Json
     */
    String requestParams;
    /**
     * 调用接口方法参数全类名
     */
    String paramName;
    /**
     * 调用接口的方法名
     */
    String name;
    /**
     * 接口url(实际是gate-way的暴露接口）
     */
    String url;
    /**
     * 方法
     */
    String method;
}
