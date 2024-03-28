package com.cpy.main.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.model.vo.QuestionSubmitVO;
import com.cpy.model.vo.QuestionVO;

public interface QuestionSubmitService extends IService<QuestionSubmit> {
    void validQuestionSubmit(QuestionSubmit questionSubmit);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> page);
    QuestionSubmitVO runCode(QuestionSubmit questionSubmit);
    /**
     * 根据id查询redis缓存
     */
    QuestionSubmit queryById(Long id);
}