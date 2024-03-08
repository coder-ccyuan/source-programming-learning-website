package com.cpy.main.judge.codeSandBox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author:成希德 执行代码请求类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteCodeRequest {
    private String language;
    private String code;
    private List<String> input;
}
