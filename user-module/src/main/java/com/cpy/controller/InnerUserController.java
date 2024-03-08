package com.cpy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.common.BaseResponse;
import com.cpy.common.ErrorCode;
import com.cpy.common.ResultUtils;
import com.cpy.exception.BusinessException;
import com.cpy.model.entity.User;
import com.cpy.model.vo.LoginUserVO;
import com.cpy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.cpy.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author <a href="https://github.com/licpy">程序员鱼皮</a>
 * @from <a href="https://cpy.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/inner/user")
@Slf4j
public class InnerUserController {

    @Resource
    private UserService userService;

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //从redis中取
//                Long userId = redisUtils.getUserIdFromRedis(token);
//        currentUser = this.getById(userId);

        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser=userService.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        request.getSession().setAttribute(USER_LOGIN_STATE,currentUser);
        return currentUser;
    }
    @GetMapping("/get/count")
    public Long selectCount(QueryWrapper<User> queryWrapper){
        long count = userService.count(queryWrapper);
        return count;

    }
}
