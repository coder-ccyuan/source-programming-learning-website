package com.cpy.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpy.main.mapper.InterfaceInformationMapper;
import com.cpy.main.mapper.UserInterfaceInfoMapper;
import com.cpy.main.mapper.UserMapper;
import com.cpy.main.service.UserInterfaceInfoService;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoAddRequest;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoUpdateRequest;
import com.cpy.model.entity.InterfaceInformation;
import com.cpy.model.entity.User;
import com.cpy.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 成希德
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service实现
* @createDate 2023-10-18 19:20:22
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {
    @Resource
    InterfaceInformationMapper interfaceInformationMapper;
    @Resource
    UserMapper userMapper;
    @Override
    public UserInterfaceInfo verify(UserInterfaceInfoAddRequest addRequest) {
        //验证id
        if (addRequest.getId() != null) {
            return null;
        }
        //验证interfaceId 是否存在
        QueryWrapper<InterfaceInformation> qw = new QueryWrapper<>();
        qw.eq("id", addRequest.getInterfaceId());
        Long integer = interfaceInformationMapper.selectCount(qw);
        if (integer != 1) {
            return null;
        }
        //验证userId 是否存在
        QueryWrapper<User> qw2 = new QueryWrapper<>();
        qw.eq("id", addRequest.getInterfaceId());
        Long integer2 = userMapper.selectCount(qw2);
        if (integer != 1) {
            return null;
        }
        //验证 totalNums
        if (addRequest.getLeftNums()==null||addRequest.getLeftNums()<1){
            return null;
        }
        //填充数据返回
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setUserId(addRequest.getUserId());
        userInterfaceInfo.setInterfaceId(addRequest.getInterfaceId());
        userInterfaceInfo.setTotalNums(0);
        userInterfaceInfo.setLeftNums(addRequest.getLeftNums());
        return userInterfaceInfo;
    }

    @Override
    public UserInterfaceInfo verify(UserInterfaceInfoUpdateRequest updateRequest) {
        if(updateRequest.getId()<1){
            return null;
        }
        if (updateRequest.getLeftNums()<0){
            return null;
        }
        if (updateRequest.getTotalNums()<0){
            return null;
        }
        if (updateRequest.getStatus()!=0||updateRequest.getStatus()!=1){
            return null;
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setId(updateRequest.getId());
        userInterfaceInfo.setTotalNums(updateRequest.getTotalNums());
        userInterfaceInfo.setLeftNums(updateRequest.getLeftNums());
        userInterfaceInfo.setStatus(updateRequest.getStatus());
        return userInterfaceInfo;
    }
}




