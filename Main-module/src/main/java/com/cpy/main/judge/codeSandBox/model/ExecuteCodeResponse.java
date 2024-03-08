package com.cpy.main.judge.codeSandBox.model;

import com.cpy.model.dto.questionSubmit.JudgeInfo;
import lombok.Data;

import java.util.List;

/**
 * @Author:成希德 执行代码响应类
 */
@Data
public class ExecuteCodeResponse {
    private List<String> output;
    private String message;
    private JudgeInfo judgeInfo;
}

