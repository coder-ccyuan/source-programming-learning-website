package com.cpy.controller;

import com.cpy.CodeSandBox;
import com.cpy.model.ExecuteCodeRequest;
import com.cpy.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author:成希德
 */
@RestController
@RequestMapping("sandbox")
public class MainController {
    @Resource
    CodeSandBox codeSandBox;

    @PostMapping("/nativeJavaSandbox")
    public ExecuteCodeResponse nativeJavaSandbox(@RequestBody ExecuteCodeRequest executeCodeRequest) {
        if (executeCodeRequest == null) {
            return new ExecuteCodeResponse("请求参数错误", null, null);
        }
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        return executeCodeResponse;
    }
}
