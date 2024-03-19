package com.cpy.clientApi;

import com.cpy.model.dto.judge.ExecuteCodeRequest;
import com.cpy.model.dto.judge.ExecuteCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "judge-service",path = "/api/judge/inner")
public interface JudgeClient {
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest);
}
