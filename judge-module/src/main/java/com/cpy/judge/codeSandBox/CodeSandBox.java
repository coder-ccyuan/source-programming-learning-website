package com.cpy.judge.codeSandBox;


import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;

/**
 * @Author:成希德
 */
public interface CodeSandBox {
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
