package com.cpy.api_system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.api_system.model.dto.interfaceInfo.*;
import com.cpy.api_system.model.entity.InterfaceInformation;
import com.cpy.api_system.service.InterfaceInformationService;
import com.cpy.api_system.utils.IsUser;
import com.cpy.api_system.utils.VerifyUtils;
import com.cpy.common.BaseResponse;
import com.cpy.common.DeleteRequest;
import com.cpy.common.ErrorCode;
import com.cpy.common.ResultUtils;
import com.cpy.exception.BusinessException;
import com.cpy.model.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.cpy.api_system.constants.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInformationController {
    @Resource
    InterfaceInformationService interfaceInformationService;
    @PostMapping("/add")
    public BaseResponse<Boolean> add(@RequestBody InterfaceInformationAddRequest addRequest, HttpServletRequest request){
        //校验数据是否为空
        if(addRequest==null||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据为null");
        }
        //校验具体数据
        InterfaceInformation interfaceInformation = interfaceInformationService.verify(addRequest);
        if (interfaceInformation == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数不符合格式或api名字重复");
        }
        //判断权限
        if (!IsUser.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //获取提交用户
        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        interfaceInformation.setUserId(user.getId());
        Boolean save = interfaceInformationService.save(interfaceInformation);
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
        boolean b = interfaceInformationService.removeById(deleteRequest.getId());
        if (!b){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
             }
        return ResultUtils.success(b);
    }
    @GetMapping("/query/list")
    public BaseResponse<List<InterfaceInformation>> query( String name,String method, HttpServletRequest request){
        //校验数据是否为空
        if(request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInformation> qw = new QueryWrapper<>();
        if (!VerifyUtils.verifyString(name)&&!VerifyUtils.verifyString(method)) {
            return ResultUtils.success(interfaceInformationService.list(qw));
        }
        qw.like("name", name).or().eq("method", method);
        //执行查询
        List<InterfaceInformation> list = interfaceInformationService.list(qw);
        if(list==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(list);
    }

    /**
     * 通过url查询接口（gate-way专用）
     * @param
     * @param request
     * @return
     */
    @PostMapping("/query/url")
    public InterfaceInformation queryByUrl(@RequestBody InterfaceInformationQueryRequest queryRequest, HttpServletRequest request){
        //校验数据是否为空
        if (queryRequest==null)throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String url=queryRequest.getUrl();
        if (url==null||url=="")throw new BusinessException(ErrorCode.PARAMS_ERROR);
        QueryWrapper<InterfaceInformation> qw = new QueryWrapper<>();
       qw.eq("url",url);
        List<InterfaceInformation> list = interfaceInformationService.list(qw);
        if(list==null||list.size()>1||list.size()==0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return list.get(0);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody InterfaceInformationUpdateRequest updateRequest, HttpServletRequest request){
        //校验数据是否为空
        if(updateRequest==null||request==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据为null");
        }
        //校验具体数据
        InterfaceInformation interfaceInformation = interfaceInformationService.verify(updateRequest);
        if (interfaceInformation == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数不符合格式或api名字重复");
        }
        //判断权限
        if (!IsUser.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        boolean b = interfaceInformationService.updateById(interfaceInformation);
        if (!b){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数异常");
        }
        return ResultUtils.success(b);
    }
    @PostMapping("/online")
    public BaseResponse<Boolean> online(@RequestBody InterfaceInformationOlineRequest olineRequest, HttpServletRequest request) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        //校验数据是否为空
        if(olineRequest==null||request==null||olineRequest.getId()<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据为null");
        }
        boolean admin = IsUser.isAdmin(request);
        if(!admin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"");
        }
        //调用相关接口确保接口有效再上线
        InterfaceInformation interfaceInformation = interfaceInformationService.getById(olineRequest.getId());
        //获取ak sk
        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);//        boolean login = IsUser.isLogin(request);
        if (user==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        com.cpy.api_system.utils.ApiClient apiClient = new com.cpy.api_system.utils.ApiClient(accessKey, secretKey);
        String url = interfaceInformation.getUrl();
        String method = interfaceInformation.getMethod();
        String response="";
        if (method.equalsIgnoreCase("post")) response= apiClient.remoteInvokeByPost(url, null);
        else if (method.equalsIgnoreCase("get"))response=apiClient.remoteInvokeByGet(url,null);
        else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未携带请求方法");
        }
        if (response.equals("Error request, response status: 403")){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //上线接口
        interfaceInformation.setStatus(0);
        boolean b = interfaceInformationService.updateById(interfaceInformation);
        if (!b){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数异常");
        }
        return ResultUtils.success(true);
    }
    @PostMapping("/offline")
    public BaseResponse<Boolean> offline(@RequestBody InterfaceInformationOfflineRequest offlineRequest, HttpServletRequest request){
        //校验数据是否为空
        if(offlineRequest==null||request==null||offlineRequest.getId()<0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求数据为null");
        }
        boolean admin = IsUser.isAdmin(request);
        if(!admin){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"");
        }
        InterfaceInformation interfaceInformation = interfaceInformationService.getById(offlineRequest.getId());
        interfaceInformation.setStatus(1);
        boolean b = interfaceInformationService.updateById(interfaceInformation);
        if (!b){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数异常");
        }
        return ResultUtils.success(true);
    }

    /**
     * 在线测试接口调用
     * @param invokeTestRequest
     * @param request
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping ("/invoke")
    public BaseResponse<String> invoke(@RequestBody InterfaceInvokeTestRequest invokeTestRequest, HttpServletRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (request==null||invokeTestRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInformation interfaceInformation = interfaceInformationService.getById(invokeTestRequest.getId());
        if (interfaceInformation==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (interfaceInformation.getStatus()==1){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }
        //获取ak sk
        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);//        boolean login = IsUser.isLogin(request);
        if (user==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        //动态调用接口
        //1.获取url
        String url = invokeTestRequest.getUrl();
        //2.获取请求参数
        String requestParams = invokeTestRequest.getRequestParams();
        //3.发送请求，通过url远程调用
        com.cpy.api_system.utils.ApiClient apiClient = new com.cpy.api_system.utils.ApiClient(accessKey, secretKey);
        String method = invokeTestRequest.getMethod();
        String response="";
        if (method.equalsIgnoreCase("post")) response= apiClient.remoteInvokeByPost(url, requestParams);
        else if (method.equalsIgnoreCase("get"))response=apiClient.remoteInvokeByGet(url,requestParams);
        else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"未携带请求方法");
        }
        //todo 完善response

        if (response.equals("Error request, response status: 403")){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(response);
        //不建议使用这种
       /* String requestParams = invokeTestRequest.getRequestParams();
        Gson gson = new Gson();
        String paramName = invokeTestRequest.getParamName();
        Class paramClass = Class.forName(paramName);//获得调用接口方法的参数的class对象
        Class clientClass = apiClient.getClass();//apiClient的class对象
        Object paramObject =  gson.fromJson(requestParams, paramClass);//接口方法的参数对象
        System.out.println(paramObject);
        Method invokeMethod = clientClass.getDeclaredMethod(invokeTestRequest.getName(), paramClass);
        Object responseResult = invokeMethod.invoke(apiClient, paramObject);
        if (responseResult==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }*/

    }

}
