package com.cpy.api_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpy.model.entity.User;


/**
* @author 成希德
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-10 21:58:20
*/
public interface UserService extends IService<User> {
    public User getSafetyUser(User user);
}
