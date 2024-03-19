package com.cpy.question.controller;

import com.cpy.common.BaseResponse;
import com.cpy.common.ResultUtils;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.question.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 内部调用题目 类
 * @Author:成希德
 */
@RestController
@RequestMapping("/inner/question")
public class InnerQuestionController {
    @Resource
    QuestionService questionService;
    @GetMapping("/get")
    BaseResponse<Question> getQuestionById(@RequestParam long id ){
        Question byId = questionService.getById(id);
       return ResultUtils.success(byId);
    }
}
