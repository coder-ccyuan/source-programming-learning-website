package com.cpy.clientApi;

import com.cpy.model.dto.interfaceInfo.InterfaceInformationQueryRequest;
import com.cpy.model.entity.InterfaceInformation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "API-service",path = "/api/API/inner")
public interface InterfaceClient {
    /**
     * 通过url查询接口（gate-way专用）
     * @param
     * @param
     * @return
     */
    @PostMapping("/InterfaceInfo/query/url")
    public InterfaceInformation queryByUrl(@RequestBody InterfaceInformationQueryRequest queryRequest);
}
