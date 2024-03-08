package com.cpy.main.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpy.main.mapper.InterfaceInformationMapper;
import com.cpy.main.service.InterfaceInformationService;
import com.cpy.model.dto.interfaceInfo.InterfaceInformationAddRequest;
import com.cpy.model.dto.interfaceInfo.InterfaceInformationUpdateRequest;
import com.cpy.model.entity.InterfaceInformation;
import com.cpy.utils.VerifyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 成希德
* @description 针对表【interface_information(接口信息表)】的数据库操作Service实现
* @createDate 2023-10-10 20:07:55
*/
@Service
public class InterfaceInformationServiceImpl extends ServiceImpl<InterfaceInformationMapper, InterfaceInformation>
    implements InterfaceInformationService {
    @Resource
    InterfaceInformationMapper interfaceInformationMapper;

    /**
     *
     * @param addRequest
     * @return 返回null 为校验错误，true为校验正确
     */
    @Override
    public InterfaceInformation verify(InterfaceInformationAddRequest addRequest) {
        //TODO 校验符合格式
        //校验数据
        if(!VerifyUtils.verifyString(addRequest.getRequestHead())){
            return null;
        }
        if(!VerifyUtils.verifyString(addRequest.getMethod())){
            return null;
        }
        if(!VerifyUtils.verifyString(addRequest.getUrl())){
            return null;
        }
        if(!VerifyUtils.verifyString(addRequest.getResponseHead())){
            return null;
        }
        if(!VerifyUtils.verifyString(addRequest.getName())){
            return null;
        }
        //判断name是否重复
        QueryWrapper<InterfaceInformation> qw = new QueryWrapper<>();
        qw.eq("name",addRequest.getName());
        Long integer = interfaceInformationMapper.selectCount(qw);
        if (integer>0)return null;
        //返回数据
        InterfaceInformation interfaceInformation = new InterfaceInformation();
        BeanUtil.copyProperties(addRequest,interfaceInformation);
        return interfaceInformation;
    }

    @Override
    public InterfaceInformation verify(InterfaceInformationUpdateRequest updateRequest) {
        //TODO 校验符合格式
        //校验数据
        if(!VerifyUtils.verifyString(updateRequest.getMethod())){
            return null;
        }
        if(!VerifyUtils.verifyString(updateRequest.getRequestHead())){
            return null;
        }
        if(!VerifyUtils.verifyString(updateRequest.getUrl())){
            return null;
        }
        if(!VerifyUtils.verifyString(updateRequest.getResponseHead())){
            return null;
        }
        if(!VerifyUtils.verifyString(updateRequest.getName())){
            return null;
        }
        //判断name是否重复
        QueryWrapper<InterfaceInformation> qw = new QueryWrapper<>();
        qw.eq("name",updateRequest.getName());
        Long integer = interfaceInformationMapper.selectCount(qw);
        if (integer<0)return null;
        if(updateRequest.getId()==null||updateRequest.getId()<0){
            return null;
        }
        if(updateRequest.getStatus()==null||updateRequest.getStatus()<0){
            return null;
        }
        //返回数据
        InterfaceInformation interfaceInformation = new InterfaceInformation();
        BeanUtil.copyProperties(updateRequest,interfaceInformation);
        return interfaceInformation;
    }
}




