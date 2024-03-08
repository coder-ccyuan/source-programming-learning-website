package com.cpy.model.dto.userInterfaceIfo;

import lombok.Data;

@Data
public class UserInterfaceInfoUpdateRequest {
    private Long id;

    /**
     * 用户id
     */
    private Integer totalNums;

    /**
     * 剩余调用次数
     */
    private Integer leftNums;
    private Integer status;
}
