package com.cpy.api_system.model.dto.userInterfaceIfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInterfaceInfoQueryRequest {
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
     * 0正常，1禁用
     */
    private Integer status;
}
