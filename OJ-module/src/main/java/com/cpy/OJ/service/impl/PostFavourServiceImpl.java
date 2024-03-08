package com.cpy.OJ.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpy.OJ.common.ErrorCode;
import com.cpy.OJ.exception.BusinessException;
import com.cpy.OJ.mapper.PostFavourMapper;
import com.cpy.OJ.model.entity.Post;
import com.cpy.OJ.model.entity.PostFavour;
import com.cpy.OJ.service.PostFavourService;
import com.cpy.OJ.service.PostService;
import com.cpy.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 帖子收藏服务实现
 *
 * @author <a href="https://github.com/licpy">程序员鱼皮</a>
 * @from <a href="https://cpy.icu">编程导航知识星球</a>
 */
@Service
public class PostFavourServiceImpl extends ServiceImpl<PostFavourMapper, PostFavour>
        implements PostFavourService {

    @Resource
    private PostService postService;

    @Override
    public int doPostFavour(long postId, User loginUser) {
        return 0;
    }

    /**
     * 帖子收藏
     *
     * @param
     * @param
     * @return
     */

    @Override
    public Page<Post> listFavourPostByPage(IPage<Post> page, Wrapper<Post> queryWrapper, long favourUserId) {
        if (favourUserId <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourPostByPage(page, queryWrapper, favourUserId);
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param postId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doPostFavourInner(long userId, long postId) {
        PostFavour postFavour = new PostFavour();
        postFavour.setUserId(userId);
        postFavour.setPostId(postId);
        QueryWrapper<PostFavour> postFavourQueryWrapper = new QueryWrapper<>(postFavour);
        PostFavour oldPostFavour = this.getOne(postFavourQueryWrapper);
        boolean result;
        // 已收藏
        if (oldPostFavour != null) {
            result = this.remove(postFavourQueryWrapper);
            if (result) {
                // 帖子收藏数 - 1
                result = postService.update()
                        .eq("id", postId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未帖子收藏
            result = this.save(postFavour);
            if (result) {
                // 帖子收藏数 + 1
                result = postService.update()
                        .eq("id", postId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

}




