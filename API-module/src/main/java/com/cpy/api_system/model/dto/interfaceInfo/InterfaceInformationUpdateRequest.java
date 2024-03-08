package com.cpy.api_system.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InterfaceInformationUpdateRequest implements Serializable {
    /**
     *
     */

    private Long id;

    /**
     * 接口名称
     */
    private String name;


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
