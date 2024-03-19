package com.cpy.config;

import com.cpy.clientApi.Decoder.MyDecoder;
import feign.codec.Decoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 配置类
 * @Author:成希德
 */
@Configuration
public class FeignConfig {
    @Bean
    public Decoder decoder() {
        return new MyDecoder();
    }

}
