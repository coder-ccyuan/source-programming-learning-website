package com.cpy.model.dto.userInterfaceIfo;

import lombok.Data;

@Data
public class UserInterfaceInfoAddRequest {
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
}
