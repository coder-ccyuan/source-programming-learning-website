package com.cpy.api_system.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 前端请求数据封装类
 */
@Data
public class InterfaceInformationAddRequest  implements Serializable {
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
