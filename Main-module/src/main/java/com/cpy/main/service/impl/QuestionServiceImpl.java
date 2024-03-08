package com.cpy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpy.common.ErrorCode;
import com.cpy.constant.CommonConstant;
import com.cpy.exception.BusinessException;
import com.cpy.exception.ThrowUtils;
import com.cpy.main.mapper.QuestionMapper;
import com.cpy.main.service.QuestionService;
import com.cpy.main.service.UserService;
import com.cpy.model.dto.question.QuestionQueryRequest;
import com.cpy.model.entity.Question;
import com.cpy.model.entity.User;
import com.cpy.model.vo.QuestionVO;
import com.cpy.model.vo.UserVO;
import com.cpy.utils.SqlUtils;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 成希德
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-02-02 20:57:29
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {
    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void validQuestion(Question question, boolean add) {
        if (question == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = question.getTitle();
        String content = question.getContent();
        String tags = question.getTags();
        String judgeCase = question.getJudgeCase();
        String judgeConfig = question.getJudgeConfig();
        String answer = question.getAnswer();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags,judgeCase,judgeConfig,answer), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
//        String searchText = questionQueryRequest.getSearchText();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tagList = questionQueryRequest.getTags();
        Long userId = questionQueryRequest.getUserId();
//        Long notId = questionQueryRequest.getNotId();
        // 拼接查询条件
//        if (StringUtils.isNotBlank(searchText)) {
//            queryWrapper.like("title", searchText).or().like("content", searchText);
//        }
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        if (CollectionUtils.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
//        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest) {
        Long id = questionQueryRequest.getId();
//        Long notId = questionQueryRequest.getNotId();
//        String searchText = questionQueryRequest.getSearchText();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tagList = questionQueryRequest.getTags();
//        List<String> orTagList = questionQueryRequest.getOrTags();
        Long userId = questionQueryRequest.getUserId();
        // es 起始页为 0
        long current = questionQueryRequest.getCurrent() - 1;
        long pageSize = questionQueryRequest.getPageSize();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("isDelete", 0));
        if (id != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }
//        if (notId != null) {
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", notId));
//        }
        if (userId != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("userId", userId));
        }
        // 必须包含所有标签
        if (CollectionUtils.isNotEmpty(tagList)) {
            for (String tag : tagList) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 包含任何一个标签即可
//        if (CollectionUtils.isNotEmpty(orTagList)) {
//            BoolQueryBuilder orTagBoolQueryBuilder = QueryBuilders.boolQuery();
//            for (String tag : orTagList) {
//                orTagBoolQueryBuilder.should(QueryBuilders.termQuery("tags", tag));
//            }
//            orTagBoolQueryBuilder.minimumShouldMatch(1);
//            boolQueryBuilder.filter(orTagBoolQueryBuilder);
//        }
        // 按关键词检索
//        if (StringUtils.isNotBlank(searchText)) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("title", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("description", searchText));
//            boolQueryBuilder.should(QueryBuilders.matchQuery("content", searchText));
//            boolQueryBuilder.minimumShouldMatch(1);
//        }
        // 按标题检索
        if (StringUtils.isNotBlank(title)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 按内容检索
        if (StringUtils.isNotBlank(content)) {
            boolQueryBuilder.should(QueryBuilders.matchQuery("content", content));
            boolQueryBuilder.minimumShouldMatch(1);
        }
        // 排序
        SortBuilder<?> sortBuilder = SortBuilders.scoreSort();
        if (StringUtils.isNotBlank(sortField)) {
            sortBuilder = SortBuilders.fieldSort(sortField);
            sortBuilder.order(CommonConstant.SORT_ORDER_ASC.equals(sortOrder) ? SortOrder.ASC : SortOrder.DESC);
        }
        // 分页
        PageRequest pageRequest = PageRequest.of((int) current, (int) pageSize);
        // 构造查询
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withPageable(pageRequest).withSorts(sortBuilder).build();
//        SearchHits<QuestionEsDTO> searchHits = elasticsearchRestTemplate.search(searchQuery, QuestionEsDTO.class);
        Page<Question> page = new Page<>();
//        page.setTotal(searchHits.getTotalHits());
        List<Question> resourceList = new ArrayList<>();
        // 查出结果后，从 db 获取最新动态数据（比如点赞数）
//        if (searchHits.hasSearchHits()) {
//            List<SearchHit<QuestionEsDTO>> searchHitList = searchHits.getSearchHits();
//            List<Long> questionIdList = searchHitList.stream().map(searchHit -> searchHit.getContent().getId())
//                    .collect(Collectors.toList());
//            List<Question> questionList = baseMapper.selectBatchIds(questionIdList);
//            if (questionList != null) {
//                Map<Long, List<Question>> idQuestionMap = questionList.stream().collect(Collectors.groupingBy(Question::getId));
//                questionIdList.forEach(questionId -> {
//                    if (idQuestionMap.containsKey(questionId)) {
//                        resourceList.add(idQuestionMap.get(questionId).get(0));
//                    } else {
//                        // 从 es 清空 db 已物理删除的数据
//                        String delete = elasticsearchRestTemplate.delete(String.valueOf(questionId), QuestionEsDTO.class);
//                        log.info("delete question {}", delete);
//                    }
//                });
//            }
//        }
        page.setRecords(resourceList);
        return page;
    }

    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            // 获取点赞
//            QueryWrapper<PostThumb> questionThumbQueryWrapper = new QueryWrapper<>();
//            questionThumbQueryWrapper.in("questionId", questionId);
//            questionThumbQueryWrapper.eq("userId", loginUser.getId());
//            PostThumb questionThumb = postThumbMapper.selectOne(questionThumbQueryWrapper);
//            questionVO.setHasThumb(questionThumb != null);
//            // 获取收藏
//            QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
//            questionFavourQueryWrapper.in("questionId", questionId);
//            questionFavourQueryWrapper.eq("userId", loginUser.getId());
//            QuestionFavour questionFavour = questionFavourMapper.selectOne(questionFavourQueryWrapper);
//            questionVO.setHasFavour(questionFavour != null);
//        }
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> questionIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> questionIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
//        if (loginUser != null) {
//            Set<Long> questionIdSet = questionList.stream().map(Question::getId).collect(Collectors.toSet());
//            loginUser = userService.getLoginUser(request);
//            // 获取点赞
//            QueryWrapper<QuestionThumb> questionThumbQueryWrapper = new QueryWrapper<>();
//            questionThumbQueryWrapper.in("questionId", questionIdSet);
//            questionThumbQueryWrapper.eq("userId", loginUser.getId());
//            List<QuestionThumb> questionQuestionThumbList = questionThumbMapper.selectList(questionThumbQueryWrapper);
//            questionQuestionThumbList.forEach(questionQuestionThumb -> questionIdHasThumbMap.put(questionQuestionThumb.getQuestionId(), true));
//            // 获取收藏
//            QueryWrapper<QuestionFavour> questionFavourQueryWrapper = new QueryWrapper<>();
//            questionFavourQueryWrapper.in("questionId", questionIdSet);
//            questionFavourQueryWrapper.eq("userId", loginUser.getId());
//            List<QuestionFavour> questionFavourList = questionFavourMapper.selectList(questionFavourQueryWrapper);
//            questionFavourList.forEach(questionFavour -> questionIdHasFavourMap.put(questionFavour.getQuestionId(), true));
//        }
        // 填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(question -> {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            Long userId = question.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUserVO(userService.getUserVO(user));
//            questionVO.setHasThumb(questionIdHasThumbMap.getOrDefault(question.getId(), false));
//            questionVO.setHasFavour(questionIdHasFavourMap.getOrDefault(question.getId(), false));
            return questionVO;
        }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

}




