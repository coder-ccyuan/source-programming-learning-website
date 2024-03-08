package com.cpy;


import com.cpy.model.ExecuteCodeRequest;
import com.cpy.model.ExecuteCodeResponse;

/**
 * @Author:成希德
 */
public interface CodeSandBox {
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
