package com.cpy.clientApi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.common.HttpServletRequestDTO;
import com.cpy.model.dto.user.UserSecretKeyRequest;
import com.cpy.model.entity.User;
import com.cpy.model.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@FeignClient(name = "user-service",path = "/api/user/inner")
@Component
public interface UserClient {
    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     */
    @GetMapping("/user/get/login")
     User getLoginUser();

    /**
     * 根据Id获取用户
     * @param userId
     * @return
     */
    @GetMapping("/user/get/user")
     User getById(@RequestParam Long userId);

    /**
     * 获取userVO
     * @param user
     * @return
     */
    @PostMapping("/user/get/userVo")
     UserVO getUserVO(@RequestBody User user);

    /**
     * 允许未登录获取登录用户
     * @param
     * @return
     */
    @GetMapping("/user/get/userPermitNull")
     User getLoginUserPermitNull();

    /**
     * 根据用户Id集合获取用户集合
     * @param userIdSet
     * @return
     */
    @PostMapping("/user/list/userId")
     List<User> listByIds(@RequestBody Set<Long> userIdSet);

    /**
     * 通过accessKey获取secretKey
     * @param request
     * @return
     */
    @PostMapping("/user/get/secretKey")
     String getSecretKeyByAccessKey(@RequestBody UserSecretKeyRequest request);

}
