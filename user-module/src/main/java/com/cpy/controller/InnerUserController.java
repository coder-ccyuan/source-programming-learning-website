package com.cpy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.common.BaseResponse;
import com.cpy.common.ErrorCode;
import com.cpy.common.ResultUtils;
import com.cpy.exception.BusinessException;
import com.cpy.model.dto.user.UserSecretKeyRequest;
import com.cpy.model.entity.User;
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
     * @param
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<User> getLoginUser(HttpServletRequest request ) {
        // 先判断是否已登录
        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //从redis中取
//                Long userId = redisUtils.getUserIdFromRedis(token);
//        currentUser = this.getById(userId);

        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = user.getId();
        user=userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户不存在");
        }
        return ResultUtils.success(user);
    }
    @GetMapping("/get/user")
    public BaseResponse<User> getById(@RequestParam Long userId){
        User user = userService.getById(userId);
        return ResultUtils.success(user);
    }
    @PostMapping("/get/userVo")
    public BaseResponse<UserVO> getUserVO(@RequestBody User user){
        UserVO userVO = userService.getUserVO(user);
        return ResultUtils.success(userVO);
    }

    /**
     *
     * @param
     * @return
     */
    @GetMapping("/get/userPermitNull")
    public BaseResponse<User> getLoginUserPermitNull(HttpServletRequest request){
        User loginUserPermitNull = userService.getLoginUserPermitNull(request);
        return ResultUtils.success(loginUserPermitNull);    }
    @PostMapping("/list/userId")
    public BaseResponse<List<User>> listByIds(@RequestBody Set<Long> userIdSet){
        List<User> users = userService.listByIds(userIdSet);
        return ResultUtils.success(users);
    }
    @PostMapping("/get/secretKey")
    public BaseResponse<String > getSecretKeyByAccessKey(@RequestBody UserSecretKeyRequest request){
        if (request==null||request.getAccessKey()==null||request.getAccessKey().equals("")){
            return null;
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey",request.getAccessKey());
        User one = userService.getOne(userQueryWrapper);
        if (one==null)return null;
        return ResultUtils.success(one.getSecretKey());
    }
}
