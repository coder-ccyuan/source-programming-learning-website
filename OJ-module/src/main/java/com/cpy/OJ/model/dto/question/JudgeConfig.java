package com.cpy.OJ.model.dto.question;

import lombok.Data;

/**
 * @Author:成希德
 * 题目限制配置类
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制
     */
    private Integer timeLimit;
    /**
     * 内存限制
     */
    private Integer memoryLimit;
    /**
     * 栈限制
     */
    private Integer stackLimit;
}
