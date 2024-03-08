package com.cpy.OJ.model.dto.questionSubmit;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

/**
 * @Author:成希德
 * 判题结果类
 */
@Data
public class JudgeInfo {
    /**
     * 判题结果
     */
    private List<String> messages;
    /**
     * 所用时间
     */
    private List<Long> times;
    /**
     * 所用内存
     */
    private List<Long> memories;
}
