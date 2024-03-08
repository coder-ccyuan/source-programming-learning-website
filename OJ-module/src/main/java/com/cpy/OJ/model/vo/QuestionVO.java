package com.cpy.OJ.model.vo;

import com.cpy.OJ.model.dto.question.JudgeCase;
import com.cpy.OJ.model.dto.question.JudgeConfig;
import com.cpy.OJ.model.entity.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图
 */
@Data
public class QuestionVO implements Serializable {

    private final static Gson GSON = new Gson();

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 答案
     */
    private String answer;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;
    /**
     * 判例（json数组）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 题目配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 通过数
     */
    private Integer acceptedNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 创建用户信息
     */
    private UserVO userVO;

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
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        List<String> tagList = questionVO.getTags();
        List<JudgeCase> judgeCases = questionVO.getJudgeCase();
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (tagList != null) {
            question.setTags(GSON.toJson(tagList));
        }
        if (judgeCases != null) {
            question.setJudgeCase(GSON.toJson(judgeCases));
        }
        if (judgeConfig != null) {
            question.setJudgeCase(GSON.toJson(judgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        questionVO.setTags(GSON.fromJson(question.getTags(), new TypeToken<List<String>>() {
        }.getType()));
        questionVO.setJudgeCase(GSON.fromJson(question.getJudgeCase(), new TypeToken<List<JudgeCase>>() {
        }.getType()));
        questionVO.setJudgeConfig(GSON.fromJson(question.getJudgeConfig(), new TypeToken<JudgeConfig>() {
        }.getType()));
        return questionVO;
    }
}
