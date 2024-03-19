package com.cpy.question.controller;

import com.cpy.common.BaseResponse;
import com.cpy.common.ResultUtils;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.question.service.QuestionSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 内部题目提交接口
 * @Author:成希德
 */
@RestController
@RequestMapping("inner/questionSubmit")
public class InnerQuestionSubmitController {
    @Resource
    QuestionSubmitService  questionSubmitService;
    @GetMapping("/get")
    public BaseResponse<QuestionSubmit> getQuestionSubmitById(@RequestParam long id){
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        return ResultUtils.success(questionSubmit);
    }
    @PostMapping("/update")
    BaseResponse<Boolean> updateQuestionSubmit(@RequestBody QuestionSubmit questionSubmit){
        boolean b = questionSubmitService.updateById(questionSubmit);
        return ResultUtils.success(b);
    }
}
