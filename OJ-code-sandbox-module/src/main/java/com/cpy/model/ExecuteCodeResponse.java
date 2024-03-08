package com.cpy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:成希德 执行代码响应类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {
    private String message;
    private List<String> output;
    private JudgeInfo judgeInfo;
}

