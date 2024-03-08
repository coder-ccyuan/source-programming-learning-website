package com.cpy.OJ.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cpy.OJ.common.BaseResponse;
import com.cpy.OJ.common.ErrorCode;
import com.cpy.OJ.common.ResultUtils;
import com.cpy.OJ.exception.BusinessException;
import com.cpy.OJ.exception.ThrowUtils;
import com.cpy.OJ.model.dto.questionSubmit.QuestionSubmitAddRequest;
import com.cpy.OJ.model.dto.questionSubmit.QuestionSubmitQueryRequest;
import com.cpy.OJ.model.entity.QuestionSubmit;
import com.cpy.OJ.model.vo.QuestionSubmitVO;
import com.cpy.OJ.service.QuestionSubmitService;
import com.cpy.OJ.service.UserService;
import com.cpy.model.entity.User;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    private UserService userService;

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
    public BaseResponse<QuestionSubmitVO> addQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
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
        User loginUser = userService.getLoginUser(request);
        questionSubmit.setUserId(loginUser.getId());
        //添加数据
        boolean result = questionSubmitService.save(questionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        //执行代码
        QuestionSubmitVO questionSubmitVO = questionSubmitService.runCode(questionSubmit);
        return ResultUtils.success(questionSubmitVO);
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
        questionSubmitQueryWrapper.eq("userId", userService.getLoginUser(httpServletRequest).getId());
        Page<QuestionSubmit> page = questionSubmitService.page(new Page<>(current, pageSize), questionSubmitQueryWrapper);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(page));
    }
}