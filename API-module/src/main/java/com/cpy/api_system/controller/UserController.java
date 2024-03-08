package com.cpy.api_system.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.api_system.Exception.CommonException;
import com.cpy.api_system.common.BaseResponse;
import com.cpy.api_system.common.StatuesCode;
import com.cpy.api_system.model.dto.UserInfo.UserLoginRequest;
import com.cpy.api_system.model.dto.UserInfo.UserQueryRequest;
import com.cpy.api_system.model.dto.UserInfo.UserRegisterRequest;
import com.cpy.api_system.model.dto.UserInfo.UserSecretKeyRequest;
import com.cpy.api_system.service.UserService;
import com.cpy.api_system.utils.ResultUtils;
import com.cpy.api_system.utils.VerifyUtils;
import com.cpy.model.entity.User;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cpy.api_system.constants.UserConstant.SALT;
import static com.cpy.api_system.constants.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;
    @PostMapping("/get/secretKey")
    public BaseResponse<String> getSecretKey(@RequestBody UserSecretKeyRequest request){
        String accessKey= request.getAccessKey();
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("accessKey",accessKey);
        User user = userService.getOne(qw);
        if (user==null){
            throw  new CommonException(StatuesCode.PARAMS_ERROR);
        }
        return ResultUtils.success(user.getSecretKey());

    }
    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request){
        if (request==null){
            throw new CommonException(StatuesCode.NULL_ERROR);
        }
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute(USER_LOGIN_STATE);
        if(user==null){
            throw new CommonException(StatuesCode.NO_AUTH);
        }
        userService.updateById(user);
        session.removeAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(true);
    }
    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest) {
        //检验数据
        if (userLoginRequest == null) {
            throw new CommonException(StatuesCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getUserPassword();
        if(!VerifyUtils.verifyString(userAccount)||!VerifyUtils.verifyString(password)){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名或密码为空");
        }
        Matcher matcher = Pattern.compile("[0-9A-Za-z]{4,16}").matcher(userAccount);
        if (!matcher.find()) {
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名有特殊字符");
        }
        //校验用户密码
        if(password.length()<5){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"密码不足4字符");
        }
        String MD5Password = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        userQueryWrapper.eq("userPassword", MD5Password);
        User user = userService.getOne(userQueryWrapper);
        if(user==null){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名或密码错误");
        }
        userService.updateById(user);
        //用户脱敏
        User safetyUser = userService.getSafetyUser(user);
        //记录用户状态
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(USER_LOGIN_STATE,safetyUser);
        return ResultUtils.success(safetyUser);
    }
    @GetMapping("/currentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object objUser = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) objUser;
        if (currentUser == null) {
          throw new CommonException(StatuesCode.NO_LOGIN);
        }
        long userId = currentUser.getId();
        //从数据库中查用户数据更新
        User byId = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(byId);
        return ResultUtils.success(safetyUser);
    }
    @PostMapping("/register")
    public BaseResponse<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new CommonException(StatuesCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String password = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(!VerifyUtils.verifyString(userAccount)||!VerifyUtils.verifyString(password)||!VerifyUtils.verifyString(checkPassword)){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名或密码为空");
        }
        if (!password.equals(checkPassword)){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名和密码不相同");
        }
        if (userAccount.length() < 4 || password.length() < 8 || checkPassword.length() < 8) {
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名或密码字符数不符合要求");
        }
        Matcher matcher = Pattern.compile("[0-9A-Za-z]{4,16}").matcher(userAccount);
        if (!matcher.find()) {
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名字符数不符合要求");
        }
        if (password.length()<8){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"密码字符数不符合要求");
        }
        //查询用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        Long count = userService.count(userQueryWrapper);
        if(count>=1){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"用户名已存在");
        }
        //加密
        String MD5Password = DigestUtils.md5DigestAsHex((SALT+password).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(MD5Password);
        //创建AK SK
        String accessKey="";
        String secretKey="";
        while (true) {
            accessKey = RandomUtil.randomString(20);
            secretKey = RandomUtil.randomString(20);
            QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
            userQueryWrapper1.eq("access_key", accessKey);
            Long count1 = userService.count(userQueryWrapper1);
            if (count1==0){
                break;
            }
        }
        user.setSecretKey(secretKey);
        user.setAccessKey(accessKey);
        boolean save = userService.save(user);
        if (!save)throw new CommonException(StatuesCode.SYSTEM_EXCEPTION);
        return ResultUtils.success(true);
    }
    @GetMapping("/query")
    public BaseResponse<List<User>> query( UserQueryRequest queryRequest, HttpServletRequest request){
        //校验数据是否为空
        if(queryRequest==null||request==null){
            throw new CommonException(StatuesCode.NULL_ERROR);
        }
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (!VerifyUtils.verifyString(queryRequest.getUsername()) && !VerifyUtils.verifyString(queryRequest.getNickname())) {
            return ResultUtils.success(userService.list(qw));
        }
        qw.like("username", queryRequest.getUsername()).or().like("nickname", queryRequest.getNickname());

        //执行查询
        List<User> list = userService.list(qw);
        //脱敏
        int i=0;
        for (User user : list) {
            User safetyUser = userService.getSafetyUser(user);
            list.set(i++,safetyUser);
        }
        if(list==null){
            throw new CommonException(StatuesCode.PARAMS_ERROR,"无用户信息或参数错误");
        }

        return ResultUtils.success(list);
    }

}
