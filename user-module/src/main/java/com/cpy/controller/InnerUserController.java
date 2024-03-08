package com.cpy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.common.BaseResponse;
import com.cpy.common.ErrorCode;
import com.cpy.common.ResultUtils;
import com.cpy.exception.BusinessException;
import com.cpy.model.dto.user.UserSecretKeyRequest;
import com.cpy.model.entity.User;
import com.cpy.model.vo.LoginUserVO;
import com.cpy.model.vo.UserVO;
import com.cpy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;

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
    @GetMapping("/get/user")
    public User getById(Long userId){
        User user = userService.getById(userId);
        return user;
    }
    @GetMapping("/get/userVo")
    public UserVO getUserVO(User user){
        UserVO userVO = userService.getUserVO(user);
        return userVO;
    }
    @GetMapping("/get/userPermitNull")
    public User getLoginUserPermitNull(HttpServletRequest request){
        User loginUserPermitNull = userService.getLoginUserPermitNull(request);
        return loginUserPermitNull;
    }
    @GetMapping("/list/userId")
    public List<User> listByIds(Set<Long> userIdSet){
        List<User> users = userService.listByIds(userIdSet);
        return users;
    }
    @PostMapping("/get/secretKey")
    public String getSecretKeyByAccessKey(@RequestBody UserSecretKeyRequest request){
        if (request==null||request.getAccessKey()==null||request.getAccessKey().equals("")){
            return null;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey",request.getAccessKey());
        User one = userService.getOne(userQueryWrapper);
        if (one==null)return null;
        return one.getSecretKey();

    }
}