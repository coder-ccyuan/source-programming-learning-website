package com.cpy.OJ.judge.codeSandBox;

import com.cpy.OJ.judge.codeSandBox.model.ExecuteCodeRequest;
import com.cpy.OJ.judge.codeSandBox.model.ExecuteCodeResponse;

/**
 * @Author:成希德
 */
public interface CodeSandBox {
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
