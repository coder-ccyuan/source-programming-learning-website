package com.cpy.main.judge.codeSandBox;

import com.cpy.main.judge.codeSandBox.model.ExecuteCodeRequest;
import com.cpy.main.judge.codeSandBox.model.ExecuteCodeResponse;

/**
 * @Author:成希德
 */
public interface CodeSandBox {
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
