package com.cpy.question.service.imp;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpy.clientApi.JudgeClient;
import com.cpy.clientApi.UserClient;
import com.cpy.common.ErrorCode;
import com.cpy.exception.BusinessException;
import com.cpy.exception.ThrowUtils;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import com.cpy.model.dto.question.JudgeCase;
import com.cpy.model.dto.question.JudgeConfig;
import com.cpy.model.dto.questionSubmit.JudgeInfo;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.model.entity.User;
import com.cpy.model.vo.QuestionSubmitVO;
import com.cpy.model.vo.QuestionVO;
import com.cpy.model.vo.UserVO;
import com.cpy.question.mapper.QuestionSubmitMapper;
import com.cpy.question.service.QuestionService;
import com.cpy.question.service.QuestionSubmitService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 成希德
 * @description 针对表【question_submit(题目)】的数据库操作Service实现
 * @createDate 2024-02-02 21:07:25
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    QuestionService questionService;
    @Resource
    UserClient userClient;
    @Resource
    RabbitTemplate rabbitTemplate;

    @Override
    public void validQuestionSubmit(QuestionSubmit questionSubmit) {
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        Long questionId = questionSubmit.getQuestionId();

        // 创建时，参数不能为空
        ThrowUtils.throwIf(StringUtils.isAnyBlank(language, code), ErrorCode.PARAMS_ERROR);
        // 有参数则校验
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目不存在");
        }

    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> page) {
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<QuestionSubmit> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return questionSubmitVOPage;
        }
        //填充数据
        List<QuestionSubmitVO> qsVORecords = new LinkedList<>();

        for (QuestionSubmit record : records) {
            QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
            BeanUtil.copyProperties(record, questionSubmitVO, "judgeInfo");
            Long questionId = record.getQuestionId();
            Long userId = record.getUserId();
            Question question = questionService.getById(questionId);
            //题目如果删除
            if (question==null){
                 question = new Question();
                question.setTitle("题目已删除");
            }
            QuestionVO questionVO = QuestionVO.objToVo(question);
            User user = userClient.getById(userId);
            UserVO userVO = new UserVO();
            BeanUtil.copyProperties(user, userVO);
            questionSubmitVO.setQuestionVO(questionVO);
            questionSubmitVO.setUserVO(userVO);
            questionSubmitVO.setJudgeInfo(JSONUtil.toBean(record.getJudgeInfo(), JudgeInfo.class));
            qsVORecords.add(questionSubmitVO);
        }
        questionSubmitVOPage.setRecords(qsVORecords);
        return questionSubmitVOPage;
    }


    /**
     * 执行代码并返回结果
     *
     * @param id 题目提交记录 id
     * @return
     */
    @Override
    public void judge(long id) {
        //使用消息队列进行
        rabbitTemplate.convertAndSend("judge.code","code",id);
    }
}




