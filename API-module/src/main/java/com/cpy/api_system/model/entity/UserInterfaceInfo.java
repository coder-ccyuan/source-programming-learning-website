package com.cpy.api_system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口信息表
 * @TableName user_interface_info
 */
@TableName(value ="user_interface_info")
@Data
public class UserInterfaceInfo implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 总调用次数
     */
    private Integer totalNums;

    /**
     * 剩余调用次数
     */
    private Integer leftNums;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 0正常，1禁用
     */
    private Integer status;

    /**
     * 
     */
    @TableLogic(value = "0",delval = "1")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}