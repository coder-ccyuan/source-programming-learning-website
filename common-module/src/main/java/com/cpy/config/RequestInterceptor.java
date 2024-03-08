package com.cpy.config;

import com.cpy.common.ErrorCode;
import com.cpy.exception.BusinessException;
import com.cpy.utils.RedisUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:成希德
 * 请求拦截器
 */
//todo 配置拦截
//@Configuration
public class RequestInterceptor implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String token = request.getHeader("token");
                if (token==null&& new RedisUtils().getUserIdFromRedis(token)==null){
                   throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"token不存在，请登录");
                }
                return true;
            }
        }).excludePathPatterns("user/get/login","/api/user/register","/api/user/login");
    }
}
