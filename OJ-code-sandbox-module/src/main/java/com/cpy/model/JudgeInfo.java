package com.cpy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:成希德 判例信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeInfo {
    /**
     * 每次判题结果
     * 运行成功
     * 运行超时
     * 爆内存
     */
    private List<String> messages;
    /**
     * 所用时间
     */
    private List<Long> times;
    /**
     * 所用内存
     */
    private List<Long> memory;
}
