package com.cpy.api_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpy.model.dto.interfaceInfo.*;
import com.cpy.model.entity.InterfaceInformation;


/**
* @author 成希德
* @description 针对表【interface_information(接口信息表)】的数据库操作Service
* @createDate 2023-10-10 20:07:55
*/
public interface InterfaceInformationService extends IService<InterfaceInformation> {
 InterfaceInformation verify(InterfaceInformationAddRequest addRequest);
     InterfaceInformation verify(InterfaceInformationUpdateRequest updateRequest);
}
