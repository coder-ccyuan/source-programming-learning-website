package com.cpy.apigateway.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 * @TableName interface_information
 */
@Data
public class InterfaceInformation implements Serializable {
    /**
     * 
     */

    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private String requestHead;

    /**
     * 
     */
    private String responseHead;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private Integer status;

    /**
     *
     */
    private Integer isDelete;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private String method;
    private String paramName;
    private static final long serialVersionUID = 1L;
}