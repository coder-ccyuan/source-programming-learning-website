package com.cpy.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息表
 * @TableName interface_information
 */
@TableName(value ="interface_information")
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
     * 接口介绍
     */
    @TableField(value = "introduce")
    private String introduce;

    /**
     * sdk的jar包地址
     */
    @TableField(value = "sdkURL")
    private String sdkURL;

    /**
     * maven依赖
     */
    @TableField(value = "maven")
    private String maven;

    /**
     *
     */
    @TableLogic(value = "0", delval = "1")
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
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}