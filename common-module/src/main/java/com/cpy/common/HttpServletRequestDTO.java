package com.cpy.common;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @Author:成希德
 */
@Data
public class HttpServletRequestDTO implements Serializable {
    HttpServletRequest httpServletRequest;
}
