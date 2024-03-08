package com.cpy.clientApi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpy.model.dto.user.UserSecretKeyRequest;
import com.cpy.model.entity.User;
import com.cpy.model.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@FeignClient(name = "user-service",path = "/api/user/inner")
@Component
public interface UserClient {
    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/user/get/login")
    public User getLoginUser(HttpServletRequest request);

    /**
     * 获取该用户数量
     * @param queryWrapper
     * @return
     */
    @GetMapping("/user/get/count")
    public Long selectCount(QueryWrapper<User> queryWrapper);

    /**
     * 根据Id获取用户
     * @param userId
     * @return
     */
    @GetMapping("/user/get/user")
    public User getById(Long userId);

    /**
     * 获取userVO
     * @param user
     * @return
     */
    @GetMapping("/user/get/userVo")
    public UserVO getUserVO(User user);

    /**
     * 允许未登录获取登录用户
     * @param request
     * @return
     */
    @GetMapping("/user/get/userPermitNull")
    public User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 根据用户Id集合获取用户集合
     * @param userIdSet
     * @return
     */
    @GetMapping("/user/list/userId")
    public List<User> listByIds(Set<Long> userIdSet);

    /**
     * 通过accessKey获取secretKey
     * @param request
     * @return
     */
    @PostMapping("/user/get/secretKey")
    public String getSecretKeyByAccessKey(@RequestBody UserSecretKeyRequest request);
}
