package com.cpy.api_system.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class InterfaceInformationQueryRequest implements Serializable {
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
    private Integer status;

    /**
     *
     */
    private String method;
    private String url;
}
