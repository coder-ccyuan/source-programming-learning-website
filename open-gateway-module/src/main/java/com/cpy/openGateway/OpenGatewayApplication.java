package com.cpy.openGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OpenGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenGatewayApplication.class,args);
    }
}