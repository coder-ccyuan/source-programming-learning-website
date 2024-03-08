package com.cpy.api_system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cpy.api_system.dao.mapper.UserMapper;
import com.cpy.api_system.service.UserService;
import com.cpy.model.entity.User;
import org.springframework.stereotype.Service;

/**
* @author 成希德
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-10-10 21:58:20
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Override
    public User getSafetyUser(User user){
        if(user==null)return null;
        User safetyUser=new User();
        safetyUser.setId(user.getId());
        BeanUtil.copyProperties(user,safetyUser,"userPassword");
        return safetyUser;
    }
}




