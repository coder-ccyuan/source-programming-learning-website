package com.cpy.api_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.api_system.Exception.CommonException;
import com.cpy.api_system.common.BaseResponse;
import com.cpy.api_system.common.DeleteRequest;
import com.cpy.api_system.common.StatuesCode;

import com.cpy.api_system.model.dto.userInterfaceIfo.UserInterfaceInfoAddRequest;
import com.cpy.api_system.model.dto.userInterfaceIfo.UserInterfaceInfoQueryRequest;
import com.cpy.api_system.model.dto.userInterfaceIfo.UserInterfaceInfoUpdateRequest;

import com.cpy.api_system.model.entity.UserInterfaceInfo;
import com.cpy.api_system.service.UserInterfaceInfoService;
import com.cpy.api_system.utils.IsUser;
import com.cpy.api_system.utils.ResultUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/user_Interface")
@RestController
public class userInterfaceInfoController {
    @Resource
    UserInterfaceInfoService userInterfaceInfoService;
    @PostMapping("/add")
    public BaseResponse<Boolean> add(@RequestBody UserInterfaceInfoAddRequest addRequest, HttpServletRequest request){
        //校验数据是否为空
        if (addRequest == null || request == null) {
            throw new CommonException(StatuesCode.NULL_ERROR, "请求数据为null");
        }
        //判断登录
        if (!IsUser.isLogin(request)) {
            throw new CommonException(StatuesCode.NO_LOGIN, "没有登录");
        }
        //校验具体数据
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.
                verify(addRequest);
        if (userInterfaceInfo == null) {
            throw new CommonException(StatuesCode.PARAMS_ERROR, "请求参数不符合格式或api名字重复");
        }


        Boolean save = userInterfaceInfoService.save(userInterfaceInfo);
        return ResultUtils.success(save);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        //校验数据是否为空
        if(deleteRequest==null||request==null){
            throw new CommonException(StatuesCode.NULL_ERROR);
        }
        //校验具体数据
        if (deleteRequest.getId() == null || deleteRequest.getId() < 0) {
            throw new CommonException(StatuesCode.PARAMS_ERROR);
        }
        //判断权限
        if(!IsUser.isAdmin(request)){
            throw new CommonException(StatuesCode.NO_AUTH);
        }
        //执行删除
        boolean b = userInterfaceInfoService.removeById(deleteRequest.getId());
        if (!b){
            throw new CommonException(StatuesCode.PARAMS_ERROR);
        }
        return ResultUtils.success(b);
    }
    @GetMapping("/query")
    public BaseResponse<List<UserInterfaceInfo>> query(UserInterfaceInfoQueryRequest queryRequest, HttpServletRequest request){
        //校验数据是否为空
        if(queryRequest==null||request==null){
            throw new CommonException(StatuesCode.NULL_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> qw = new QueryWrapper<>();
        if (queryRequest.getId() == null
                && queryRequest.getInterfaceId()==null&&queryRequest.getUserId()==null&&queryRequest.getStatus()==null  ) {
            return ResultUtils.success(userInterfaceInfoService.list(qw));
        }
        qw.eq("id", queryRequest.getId()).or().eq("interfaceId", queryRequest.getInterfaceId()).or().eq("status", queryRequest.getStatus()).or().eq("userId", queryRequest.getUserId());
        //执行查询
        List<UserInterfaceInfo> list = userInterfaceInfoService.list(qw);
        if(list==null){
            throw new CommonException(StatuesCode.PARAMS_ERROR);
        }

        return ResultUtils.success(list);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody UserInterfaceInfoUpdateRequest updateRequest, HttpServletRequest request){
        //校验数据是否为空
        if(updateRequest==null||request==null){
            throw new CommonException(StatuesCode.NULL_ERROR,"请求数据为null");
        }
        //校验具体数据
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.verify(updateRequest);
        if (userInterfaceInfo == null) {
            throw new CommonException(StatuesCode.PARAMS_ERROR,"请求参数不符合格式或数值范围错误");
        }
        //判断权限
        if (!IsUser.isLogin(request)) {
            throw new CommonException(StatuesCode.NO_AUTH);
        }
        //更新
        boolean b = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(b);
    }
}
