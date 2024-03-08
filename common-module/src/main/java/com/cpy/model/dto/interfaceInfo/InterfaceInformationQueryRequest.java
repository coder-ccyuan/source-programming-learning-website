package com.cpy.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

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
