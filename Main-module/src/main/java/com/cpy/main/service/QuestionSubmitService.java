package com.cpy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.model.vo.QuestionSubmitVO;

public interface QuestionSubmitService extends IService<QuestionSubmit> {
    void validQuestionSubmit(QuestionSubmit questionSubmit);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> page);
    QuestionSubmitVO runCode(QuestionSubmit questionSubmit);
}