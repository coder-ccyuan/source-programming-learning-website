package com.cpy.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpy.model.dto.interfaceInfo.InterfaceInformationAddRequest;
import com.cpy.model.dto.interfaceInfo.InterfaceInformationUpdateRequest;
import com.cpy.model.entity.InterfaceInformation;


/**
 * @author 成希德
 * @description 针对表【interface_information(接口信息表)】的数据库操作Service
 * @createDate 2023-10-10 20:07:55
 */
public interface InterfaceInformationService extends IService<InterfaceInformation> {
    InterfaceInformation verify(InterfaceInformationAddRequest addRequest);

    InterfaceInformation verify(InterfaceInformationUpdateRequest updateRequest);
    /**
     * 通过id从redis中获取
     */
    InterfaceInformation queryById(Long id);
}
