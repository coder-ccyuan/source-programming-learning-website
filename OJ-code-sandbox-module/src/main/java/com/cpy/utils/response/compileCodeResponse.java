package com.cpy.utils.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author:成希德
 * 代码编译响应类
 */
@Data
@AllArgsConstructor
public class compileCodeResponse {
    private String message;
    private boolean flag;
}
