package com.cpy.judge.rabbitCustomer;

import cn.hutool.json.JSONUtil;
import com.cpy.clientApi.QuestionClient;
import com.cpy.common.ErrorCode;
import com.cpy.exception.BusinessException;
import com.cpy.judge.codeSandBox.CodeSandBox;
import com.cpy.judge.utils.JudgeUtil;
import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import com.cpy.model.dto.question.JudgeCase;
import com.cpy.model.dto.question.JudgeConfig;
import com.cpy.model.dto.questionSubmit.JudgeInfo;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.QuestionSubmit;
import com.cpy.model.vo.QuestionSubmitVO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author:成希德
 */
@Component
public class JudgeConsumer {
    @Resource
    CodeSandBox codeSandBox;
    @Resource
    QuestionClient questionClient;
    @RabbitListener(queues = "judge.queue")
    public void listenJudgeQueue(long id){
        QuestionSubmit questionSubmit = questionClient.getQuestionSubmitById(id);
        Question question = questionClient.getQuestionById(questionSubmit.getQuestionId());
        //封装请求
        ExecuteCodeRequest executeCodeRequest = JudgeUtil.getExecuteCodeRequest(questionSubmit, question);
        //更改状态判题中
        questionSubmit.setStatus(1);
        questionClient.updateQuestionSubmit(questionSubmit);
        //执行代码
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
            //获取判题判题信息
            List<String> questionInputs = JudgeUtil.getQuestionInputs(question);//输入案例
            List<String> questionOutputs = JudgeUtil.getQuestionOutputs(question);//输出案例
            //获取判题结果信息
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
          /*      Long memory = memories.get(i);
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
            //判题失败
            questionSubmit.setStatus(3);
        } else {
            //判题成功
            Integer acceptedNum = question.getAcceptedNum();
            question.setAcceptedNum(++acceptedNum);
            questionClient.updateQuestion(question);
            questionSubmit.setStatus(2);
        }
        //填充数据
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean save =  questionClient.updateQuestionSubmit(questionSubmit);
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
    }
}
