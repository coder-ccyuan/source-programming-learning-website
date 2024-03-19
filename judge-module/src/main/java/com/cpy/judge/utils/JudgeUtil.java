package com.cpy.judge.utils;

import cn.hutool.json.JSONUtil;
import com.cpy.clientApi.QuestionClient;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import com.cpy.model.dto.question.JudgeCase;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.QuestionSubmit;

import java.util.LinkedList;
import java.util.List;

/**
 * 判题相关工具类
 */
public class JudgeUtil {
    /**
     *
     * @param questionSubmit 判题记录
     * @param question 判题记录里的题目
     * @return
     */
    public static ExecuteCodeRequest getExecuteCodeRequest(QuestionSubmit questionSubmit,Question question){
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setLanguage(questionSubmit.getLanguage());
        executeCodeRequest.setCode(questionSubmit.getCode());
        //获取inputs,outputs
        List<String> questionInputs = new LinkedList<>();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        for (JudgeCase jc : judgeCases) {
            questionInputs.add(jc.getInput());
        }
        executeCodeRequest.setInput(questionInputs);
        return executeCodeRequest;
    }

    /**
     * 获取题目输入案例
     * @param question 题目
     * @return
     */
    public static List<String>getQuestionOutputs(Question question){
        List<String> questionOutputs = new LinkedList<>();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        for (JudgeCase aCase : judgeCases) {
            questionOutputs.add(aCase.getOutput());
        }
        return questionOutputs;
    }

    /**
     * 获取题目输出案例
     * @param question 题目
     * @return
     */
    public static List<String>getQuestionInputs(Question question){
        List<String> questionInputs = new LinkedList<>();
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCases = JSONUtil.toList(judgeCase, JudgeCase.class);
        for (JudgeCase aCase : judgeCases) {
            questionInputs.add(aCase.getOutput());
        }
        return questionInputs;
    }
}
