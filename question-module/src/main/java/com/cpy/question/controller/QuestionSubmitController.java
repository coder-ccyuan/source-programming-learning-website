package com.cpy.question.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cpy.clientApi.UserClient;
import com.cpy.common.BaseResponse;
import com.cpy.common.ErrorCode;
import com.cpy.common.ResultUtils;
import com.cpy.exception.BusinessException;
import com.cpy.exception.ThrowUtils;
import com.cpy.model.dto.questionSubmit.JudgeInfo;
import com.cpy.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.cpy.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.model.entity.User;
import com.cpy.model.vo.QuestionSubmitVO;
import com.cpy.question.service.QuestionSubmitService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/licpy">程序员鱼皮</a>
 * @from <a href="https://cpy.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/questionSubmit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserClient userClient;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 添加判题信息
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        if (questionSubmitAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();

        BeanUtils.copyProperties(questionSubmitAddRequest, questionSubmit);
        String judgeInfo = questionSubmit.getJudgeInfo();
        if (judgeInfo != null) {
            questionSubmit.setJudgeInfo(GSON.toJson(judgeInfo));
        }
        //校验数据
        questionSubmitService.validQuestionSubmit(questionSubmit);
        User loginUser = userClient.getLoginUser();
        questionSubmit.setUserId(loginUser.getId());
        //添加数据
        boolean result = questionSubmitService.save(questionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        //进行判题
        questionSubmitService.judge(questionSubmit.getId());
        return ResultUtils.success(questionSubmit.getId());
    }

    /**
     * 题目提交信息页面
     *
     * @return
     */
    @GetMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitVOByPage(QuestionSubmitQueryRequest request) {
        long current = request.getCurrent();
        long pageSize = request.getPageSize();
        Page<QuestionSubmit> page = questionSubmitService.page(new Page<>(current, pageSize));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(page));
    }

    @GetMapping("/list/page/myVo")
    public BaseResponse<Page<QuestionSubmitVO>> myListQuestionSubmitVOByPage(QuestionSubmitQueryRequest request, HttpServletRequest httpServletRequest) {
        if (request == null || httpServletRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = request.getCurrent();
        long pageSize = request.getPageSize();
        QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
        questionSubmitQueryWrapper.eq("userId", userClient.getLoginUser().getId());
        Page<QuestionSubmit> page = questionSubmitService.page(new Page<>(current, pageSize), questionSubmitQueryWrapper);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(page));
    }
    @GetMapping("/get/vo")
    public BaseResponse<QuestionSubmitVO> getQuestionVOById(@RequestParam long id) throws InterruptedException {
        //线程睡2s,方便判题
        Thread.sleep(2000);
        if (id<0)throw new BusinessException(ErrorCode.PARAMS_ERROR);
        QuestionSubmit questionSubmit = questionSubmitService.getById(id);
        if (questionSubmit==null)throw new BusinessException(ErrorCode.PARAMS_ERROR);
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        if (questionSubmitVO==null)throw new BusinessException(ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(questionSubmitVO);
    }
}