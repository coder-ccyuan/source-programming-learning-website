package com.cpy.api_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.api_system.service.InterfaceInformationService;
import com.cpy.common.ErrorCode;
import com.cpy.exception.BusinessException;
import com.cpy.model.dto.interfaceInfo.InterfaceInformationQueryRequest;
import com.cpy.model.entity.InterfaceInformation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @Author:成希德
 */
@RequestMapping("/inner/interfaceInfo")
public class InnerInterfaceInfoController {
    @Resource
    InterfaceInformationService interfaceInformationService;
    /**
     * 通过url查询接口（gate-way专用）
     * @param
     * @param
     * @return
     */
    @PostMapping("/query/url")
    public InterfaceInformation queryByUrl(@RequestBody InterfaceInformationQueryRequest queryRequest){
        //校验数据是否为空
        if (queryRequest==null)throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String url=queryRequest.getUrl();
        if (url==null||url=="")throw new BusinessException(ErrorCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInformation> qw = new QueryWrapper<>();
        qw.eq("url",url);
        InterfaceInformation interfaceInformation = interfaceInformationService.getOne(qw);
        return interfaceInformation;
    }
}
