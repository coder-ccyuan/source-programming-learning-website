package com.cpy.main.judge.codeSandBox.imp;

import com.cpy.main.judge.codeSandBox.CodeSandBox;
import com.cpy.model.dto.questionSubmit.JudgeInfo;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;

/**
 * @Author:成希德
 */
public class ExampleCodeSandbox implements CodeSandBox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutput(request.getInput());
        executeCodeResponse.setMessage(request.getCode());
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
