package com.cpy.api_system.model.dto.userInterfaceIfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UserInterfaceInfoAddRequest {
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
}
