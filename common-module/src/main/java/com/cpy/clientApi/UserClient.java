package com.cpy.clientApi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.common.BaseResponse;
import com.cpy.common.ResultUtils;
import com.cpy.model.entity.User;
import com.cpy.model.vo.LoginUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@FeignClient("user-service")
@Component
public interface UserClient {
    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("inner/user/get/login")
    public User getLoginUser(HttpServletRequest request);

    /**
     * 获取该用户数量
     * @param queryWrapper
     * @return
     */
    @GetMapping("/get/count")
    public Long selectCount(QueryWrapper<User> queryWrapper);
}
