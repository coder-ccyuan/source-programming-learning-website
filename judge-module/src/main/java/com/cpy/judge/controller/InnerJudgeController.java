package com.cpy.judge.controller;

import com.cpy.common.BaseResponse;
import com.cpy.common.ResultUtils;
import com.cpy.judge.codeSandBox.CodeSandBox;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @Author:成希德
 */
@RestController
@RequestMapping("/inner")
public class InnerJudgeController {
    @Resource
    CodeSandBox codeSandBox;
    @PostMapping("/executeCode")
    public BaseResponse<ExecuteCodeResponse> executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest){
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        return ResultUtils.success(executeCodeResponse);
    }

}
