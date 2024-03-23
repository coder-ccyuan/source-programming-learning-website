package com.cpy.main.judge.codeSandBox.imp;


import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.cpy.main.judge.codeSandBox.CodeSandBox;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import org.springframework.stereotype.Service;

/**
 * @Author:成希德
 */
@Service
public class NativeJavaSandbox implements CodeSandBox {
    public static final String NATIVE_SANDBOX_URL="localhost:8091/sandbox/nativeJavaSandbox";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String body = JSONUtil.toJsonStr(executeCodeRequest);
        String response = HttpRequest.post(NATIVE_SANDBOX_URL).body(body).execute().body();
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(response, ExecuteCodeResponse.class);
        return executeCodeResponse;
    }
}
