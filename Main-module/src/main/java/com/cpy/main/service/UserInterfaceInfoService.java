package com.cpy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoAddRequest;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoUpdateRequest;
import com.cpy.model.entity.UserInterfaceInfo;

/**
* @author 成希德
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service
* @createDate 2023-10-18 19:20:22
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    public UserInterfaceInfo verify(UserInterfaceInfoAddRequest addRequest);
    public UserInterfaceInfo verify(UserInterfaceInfoUpdateRequest updateRequest);
    public UserInterfaceInfo queryById(Long id);
}
