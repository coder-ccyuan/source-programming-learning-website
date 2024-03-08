package com.cpy.api_system.model.dto.UserInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserQueryRequest implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     *  性别，0为女，1为男
     */
    private Integer gender;

    /**
     *  0为离线，1为上线
     */
    private Integer state;

}
