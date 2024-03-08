package com.cpy.model.dto.userInterfaceIfo;

import lombok.Data;

@Data
public class UserInterfaceInfoQueryRequest {
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
     * 0正常，1禁用
     */
    private Integer status;
}
