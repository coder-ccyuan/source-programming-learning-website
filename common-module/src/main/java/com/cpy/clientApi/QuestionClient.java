package com.cpy.clientApi;

import com.cpy.common.BaseResponse;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "question-service",path = "/api/question/inner")
public interface QuestionClient {
    @GetMapping("/questionSubmit/get")
     QuestionSubmit getQuestionSubmitById(@RequestParam long id);
    @PostMapping("/questionSubmit/update")
    Boolean updateQuestionSubmit(@RequestBody QuestionSubmit questionSubmit);
    @GetMapping("/question/get")
    Question getQuestionById(@RequestParam long id);
}
