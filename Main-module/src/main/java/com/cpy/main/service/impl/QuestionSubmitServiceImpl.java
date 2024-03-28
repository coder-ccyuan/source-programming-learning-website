package com.cpy.main.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.cpy.constant.QuestionConstant;
import com.cpy.constant.QuestionSubmitConstant;
import com.cpy.main.judge.codeSandBox.CodeSandBox;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import com.cpy.main.mapper.QuestionSubmitMapper;
import com.cpy.main.service.QuestionService;
import com.cpy.main.service.QuestionSubmitService;
import com.cpy.main.service.UserService;
import com.cpy.model.dto.question.JudgeCase;
import com.cpy.model.dto.question.JudgeConfig;
import com.cpy.model.dto.questionSubmit.JudgeInfo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpy.common.ErrorCode;
import com.cpy.exception.BusinessException;
import com.cpy.exception.ThrowUtils;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.model.entity.User;
import com.cpy.model.vo.QuestionSubmitVO;
import com.cpy.model.vo.QuestionVO;
import com.cpy.model.vo.UserVO;
import com.cpy.utils.RedisUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    UserService userService;
    @Resource
    CodeSandBox codeSandBox;
    @Resource
    private RedisUtils redisUtils;

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
            QuestionVO questionVO = new QuestionVO();
            BeanUtil.copyProperties(question,questionVO,"judgeConfig","judgeCase");
            if (question==null)questionVO.setTitle("题目已删除");
            User user = userService.getById(userId);
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
     * @param questionSubmit
     * @return
     */
    @Override
    public QuestionSubmitVO runCode(QuestionSubmit questionSubmit) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setLanguage(questionSubmit.getLanguage());
        executeCodeRequest.setCode(questionSubmit.getCode());
        Question question = questionService.getById(questionSubmit.getQuestionId());
        //获取inputs,outputs
        List<String> questionInputs = new LinkedList<>();
        List<String> questionOutputs = new LinkedList<>();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        for (JudgeCase jc : judgeCases) {
            questionInputs.add(jc.getInput());
            questionOutputs.add(jc.getOutput());
        }
        executeCodeRequest.setInput(questionInputs);
        questionSubmit.setStatus(1);
        this.updateById(questionSubmit);
        ExecuteCodeResponse executeCodeResponse = codeSandBox.executeCode(executeCodeRequest);
        //校验响应结果和更改状态填充数据
        //获取响应结果
        List<String> sandboxOutputs = executeCodeResponse.getOutput();
        String message = executeCodeResponse.getMessage();
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();
        //校验数据
        boolean flag = true;//标记题目是否正确
        if (!message.equals("运行成功")) {
            //更改状态
            flag = false;
        } else {
            //获取判题信息
            List<String> messages = judgeInfo.getMessages();
            List<Long> times = judgeInfo.getTimes();
            List<Long> memories = judgeInfo.getMemories();
            String judgeConfigJson = question.getJudgeConfig();
            JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigJson, JudgeConfig.class);
            Integer timeLimit = judgeConfig.getTimeLimit();
            Integer memoryLimit = judgeConfig.getMemoryLimit();
            int n = questionInputs.size();//测试数据的个数
            for (int i = 0; i < n; i++) {
                //时间校验
                Long time = times.get(i);
                if (time > timeLimit) {
                    messages.set(i, "运行超时");
                    flag = false;
                    continue;
                }
                //todo 对内存校验
             /*   Long memory = memories.get(i);
                if (memory > memoryLimit) {
                    messages.set(i, "内存超限");
                    flag = false;
                    continue;
                }*/
                //对结果校验
                String s = sandboxOutputs.get(i);
                String s1 = questionOutputs.get(i);
                if (!s.equals(s1)) {
                    messages.set(i, "答案错误");
                    flag = false;
                }
            }
        }
        if (!flag) {
            questionSubmit.setStatus(3);
        } else {
            questionSubmit.setStatus(2);
        }
        //填充数据
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean save = this.updateById(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交数据更新失败");
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        questionSubmitVO.setId(questionSubmit.getId());
        questionSubmitVO.setLanguage(questionSubmit.getLanguage());
        questionSubmitVO.setCode(questionSubmit.getCode());
        questionSubmitVO.setJudgeInfo(judgeInfo);
        questionSubmitVO.setUserId(questionSubmit.getUserId());
        questionSubmitVO.setQuestionId(question.getId());
        questionSubmitVO.setStatus(questionSubmit.getStatus());
        questionSubmitVO.setUserVO(userService.getUserVO(userService.getById(questionSubmit.getUserId())));
        return questionSubmitVO;
    }

    @Override
    public QuestionSubmit queryById(Long id) {
        QuestionSubmit questionSubmit = redisUtils.queryWithPassThrough(QuestionSubmitConstant.CACHE_QUESTION_SUBMIT_KEY, id, QuestionSubmit.class, this::getById, RandomUtil.randomLong(10000), TimeUnit.SECONDS);
        return questionSubmit;
    }
}



