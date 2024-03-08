package com.cpy.OJ.judge.codeSandBox.imp;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cpy.OJ.judge.codeSandBox.CodeSandBox;
import com.cpy.OJ.judge.codeSandBox.model.ExecuteCodeRequest;
import com.cpy.OJ.judge.codeSandBox.model.ExecuteCodeResponse;
import org.springframework.stereotype.Service;

/**
 * @Author:成希德
 */
@Service
public class NativeJavaSandbox implements CodeSandBox {

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String url="localhost:8091/sandbox/nativeJavaSandbox";
        String body = JSONUtil.toJsonStr(executeCodeRequest);
        String response = HttpRequest.post(url).body(body).execute().body();
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(response, ExecuteCodeResponse.class);
        return executeCodeResponse;
    }
}
