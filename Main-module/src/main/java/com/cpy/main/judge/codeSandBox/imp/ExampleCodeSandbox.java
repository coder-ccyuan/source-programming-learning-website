package com.cpy.main.judge.codeSandBox.imp;

import com.cpy.main.judge.codeSandBox.CodeSandBox;
import com.cpy.main.judge.codeSandBox.model.ExecuteCodeRequest;
import com.cpy.main.judge.codeSandBox.model.ExecuteCodeResponse;
import com.cpy.model.dto.questionSubmit.JudgeInfo;

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
