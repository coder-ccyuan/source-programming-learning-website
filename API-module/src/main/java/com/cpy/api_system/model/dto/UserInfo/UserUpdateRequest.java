package com.cpy.api_system.model.dto.UserInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {
    private Integer id;
    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     *  性别，0为女，1为男
     */
    private Integer gender;

}
