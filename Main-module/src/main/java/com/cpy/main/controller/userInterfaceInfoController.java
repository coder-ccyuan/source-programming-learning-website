package com.cpy.main.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.common.BaseResponse;
import com.cpy.common.DeleteRequest;
import com.cpy.common.ErrorCode;
import com.cpy.common.ResultUtils;
import com.cpy.exception.BusinessException;
import com.cpy.main.service.UserInterfaceInfoService;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoAddRequest;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoQueryRequest;
import com.cpy.model.dto.userInterfaceIfo.UserInterfaceInfoUpdateRequest;
import com.cpy.model.entity.UserInterfaceInfo;
import com.cpy.utils.IsUser;
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求数据为null");
        }
        //判断登录
        if (!IsUser.isLogin(request)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "没有登录");
        }
        //校验具体数据
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.
                verify(addRequest);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不符合格式或api名字重复");
        }


        Boolean save = userInterfaceInfoService.save(userInterfaceInfo);
        return ResultUtils.success(save);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        //校验数据是否为空
        if(deleteRequest==null||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验具体数据
        if (deleteRequest.getId() == null || deleteRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断权限
        if(!IsUser.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //执行删除
        boolean b = userInterfaceInfoService.removeById(deleteRequest.getId());
        if (!b){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(b);
    }
    @GetMapping("/query")
    public BaseResponse<List<UserInterfaceInfo>> query(UserInterfaceInfoQueryRequest queryRequest, HttpServletRequest request){
        //校验数据是否为空
        if(queryRequest==null||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
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
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(list);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody UserInterfaceInfoUpdateRequest updateRequest, HttpServletRequest request){
        //校验数据是否为空
        if(updateRequest==null||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据为null");
        }
        //校验具体数据
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.verify(updateRequest);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数不符合格式或数值范围错误");
        }
        //判断权限
        if (!IsUser.isLogin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //更新
        boolean b = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(b);
    }
}
