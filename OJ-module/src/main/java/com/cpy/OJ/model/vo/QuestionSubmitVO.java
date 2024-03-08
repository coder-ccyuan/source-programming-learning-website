package com.cpy.OJ.model.vo;

import com.cpy.OJ.model.dto.question.JudgeCase;
import com.cpy.OJ.model.dto.question.JudgeConfig;
import com.cpy.OJ.model.dto.questionSubmit.JudgeInfo;
import com.cpy.OJ.model.entity.Question;
import com.cpy.OJ.model.entity.QuestionSubmit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 提交记录视图
 */
@Data
public class QuestionSubmitVO implements Serializable {

    private final static Gson GSON = new Gson();

    private Long id;

    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    /**
     * 判例返回信息（json 数组）time、 memory、message
     */
    private JudgeInfo judgeInfo;

    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 提交用户
     */
    private UserVO userVO;
    /**
     * 题目Id
     */
    private Long questionId;
    /**
     * 题目
     */
    private QuestionVO questionVO;
    /**
     * 判题状态 0-待判题 1-判题中 2-成功 3-失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        if (judgeInfo != null) {
            questionSubmit.setJudgeInfo(GSON.toJson(judgeInfo));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        questionSubmitVO.setJudgeInfo(GSON.fromJson(questionSubmit.getJudgeInfo(), new TypeToken<JudgeInfo>() {
        }.getType()));
        return questionSubmitVO;
    }
}
