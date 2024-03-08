package com.cpy.OJ.model.dto.questionSubmit;

import com.cpy.OJ.model.dto.question.JudgeCase;
import com.cpy.OJ.model.dto.question.JudgeConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/licpy">程序员鱼皮</a>
 * @from <a href="https://cpy.icu">编程导航知识星球</a>
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {
    /**
     * 语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 题目Id
     */
    private Long questionId;


    private static final long serialVersionUID = 1L;
}