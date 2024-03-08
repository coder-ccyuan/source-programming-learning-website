package com.cpy.api_system.model.dto.userInterfaceIfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInterfaceInfoUpdateRequest {
    @TableId(type = IdType.AUTO)
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
