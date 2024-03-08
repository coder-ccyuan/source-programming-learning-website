package com.cpy.api_system.model.dto.UserInfo;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserLoginRequest implements Serializable {
    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;
}
