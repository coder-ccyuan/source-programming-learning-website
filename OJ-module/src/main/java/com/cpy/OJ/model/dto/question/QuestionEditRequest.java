package com.cpy.OJ.model.dto.question;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户编辑编辑请求
 *
 */
@Data
public class QuestionEditRequest implements Serializable {
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
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判例（json数组）
     */
    private List<JudgeCase> judgeCase;
    /**
     * 题目配置（要转成Json）
     */
    private JudgeConfig judgeConfig;
    private static final long serialVersionUID = 1L;
}