# source-programming-learning-website(一站式‘源编程开发学习站’) version 2.0  

*source* 是一站式编程开发学习平台 有两大模块组成**算法学习模块**和**API接口开放平台模块**
组成 。
- **算法学习模块** 主要是提供算法题集，用户通过刷算法题提升算法能力
- **API接口开放平台模块** 提供辅助开发的API，辅助开发者进行开发
## 功能
___
- OJ判题系统
- API接口开放平台
- 用户管理
## version 2.0 做了什么？
___
1.将version 1.0 升级为分布式  

2.分布式技术选型    
- Spring Cloud Alibaba
  - Nacos 作为服务发现&注册中心&配置中心
- Open Feign 远程调用
- Spring Gateway 网关
- RabbitMQ 消息队列
- redis 分布式session 缓存
## 开发日志
___ 
### 3.15
- 创建User 模块 √
- 将单体项目拆分模块 √
   - User 用户服务
   - OJ 题目服务和判题服务
   - API 模块
   - API-gateway 模块
   - OJ-sandBox 代码沙箱可不纳入微服务，作为远程api调用
   - common 通用服务
- 将通用的model 放入 common模块中 √
- 将通用返回类,工具类，常量，异常处理类放入common模块 √
- API module 导入common的包 √
- OJ module 重新导入common的包 √
- 增加Spring Cloud Alibaba 依赖 ，并配置相关信息 √
- 增加Open feign 依赖并配置相关信息 √
- 开发feignClient 客户端 实现远程调用
---
### 3.16
- 继续开发userClient √ 
- 开发interfaceClient √
- 实现API-gateway 的远程验证API是否存在代码的feignClient √
- 开发外部网关 open-gateway-service √
    - 断言*path*要大写,相关配置如下：
    - ```yaml
         gateway:
        routes:
          - id: user-service
            uri: lb://user-service
            predicates:
              - Path=/api/user/** #Path大写
          - id: OJ-service
            uri: lb://OJ-service
            predicates:
              - Path=/api/OJ/**
          - id: api-service
            uri:
              lb://api-service
            predicates:
              - Path=/api/API/**
      ```
- 聚合文档 √
  - 分布式聚合文档 <https://doc.xiaominfo.com/docs/middleware-sources/spring-cloud-gateway/spring-gateway-introduction>
  - 每个服务要引入(gateway不用)
  - ```xml
    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-openapi2-spring-boot-starter</artifactId>
        <version>4.3.0</version>
    </dependency>
    ```
  - gateway 要引入
  - ```xml
       <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-gateway-spring-boot-starter</artifactId>
            <version>4.3.0</version>
        </dependency>
    ```
  - 要给每个服务取个唯一的context-path，用于区分服务，否则会发生*无法获取接口错误*
  因为默认的请求的是`{host:}{port}/{context-path}/v2/api-docs?group=default`
  没有 context-path 的话，无法区分
  - 记录一个现象
    - 聚合聚合文档403错误，原因是open-gateway 先启用，没有读取到其他服务配置
    - 解决方法是最后启动
- 分布式登录的各服务的相对路径要统一 √
  - 配置path，且配置要一致，否则访问一个服务会产生多个SESSION
  - ```yaml
    session:
    #  开启分布式 session（须先配置 Redis）
    store-type: redis
    #     30 天过期
    timeout: 2592000
    path: /api
    ```
___
### 3.17

- 记录一个 bug
  - 使用 openFeign 远程调用的时候传递的参数是`QueryWrapper`类型时无法进行json相互转化，
目前没有解决方案，所以在使用openFeign进行远程调用时尽量传递简单对象
- openFeign 的注意事项
  - 参数一定要加 @RequestParam 或 @RequestBody 否者会报错
- 将 OJ-module 拆分为 Question module 模块 和 judge 模块 √
- 在 open-gateway 实现全局跨域 √
- 在 open-gateway 实现全局过滤 *inner* √
- openFeign 无法直接传递 HttpServletRequest
  - 原因是 openFeign 在请求服务时会生成一个新的 requestHeader 造成原先的 Header 信息丢失
    -解决方案： 使用 **RequestInterceptor** (注：客户端无需传递 httpServlet 参数)
    - ```java
      @Configuration
      public class RequestFeignConfig {
      @Bean("requestInterceptor")
      public RequestInterceptor requestInterceptor() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                //1、使用RequestContextHolder拿到刚进来的请求数据
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (requestAttributes != null) {
                    //老请求
                    HttpServletRequest request = requestAttributes.getRequest();
                    if (request != null) {
                        //2、同步请求头的数据（主要是cookie）
                        //把老请求的cookie值放到新请求上来，进行一个同步
                        String cookie = request.getHeader("Cookie");
                        template.header("Cookie", cookie);
                    }
                }
            }
        };

        return requestInterceptor;
      }
       }
      ```
___
### 3.18
- 记录一个错误 ``java.lang.IllegalArgumentException: Body parameter 0 was null``
  - 原因：feignClient 使用 Post 必须携带请求体，没携带报错
  - 解决：使用 Get 请求
- 实现 openFeign 调用服务时，被调用方出现异常，将结果直接返回前端 √
  - 使用 Encoder 对返回对象手动自定义解码，统一获取数据和处理异常并返回给前端
  - 注意点：``BaseResponse``要有无参构造器，反射需要
  - ```java
    package com.cpy.clientApi.Decoder;

    import com.cpy.common.BaseResponse;
    import com.cpy.exception.BusinessException;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.type.TypeFactory;
    import feign.FeignException;
    import feign.Response;
    import feign.codec.Decoder;
    import java.io.IOException;
    import java.lang.reflect.Type;
    
    /**
    * openFeign 自定义解码器
      * @Author:成希德
        */
        public class MyDecoder implements Decoder {
    
        private ObjectMapper objectMapper = new ObjectMapper();
    
        @Override
        public Object decode(Response response, Type type) throws FeignException, IOException {
    
             TypeFactory typeFactory = objectMapper.getTypeFactory();
    
             BaseResponse baseResponse = objectMapper.readValue(response.body().asInputStream(),
                     typeFactory.constructParametricType(BaseResponse.class, typeFactory.constructType(type)));
    
             if (type instanceof BaseResponse) {
                 return baseResponse;
             }
    
             if (baseResponse.getCode()==0) {
                 return baseResponse.getData();
             }
    
             throw new BusinessException(baseResponse.getCode(),baseResponse.getMessage());
        }
        }
      ```
- 后端配置跨域只能配置一个，重复配置会导致跨域失败
___
### 8.19
- 判题是比较重的操作，使用 RabbitMQ 使服务模块和判题模块解耦 √
  - 下载 rabbitMq 服务端 <https://blog.csdn.net/Relievedz/article/details/131081440>
  - 引入 rabbitMq 依赖,使用 SpringAMQP 封装好的 RabbitMQ 模板
  - 生成 exchange queue 并让 judge 模块 监听 queue
- 记录一个坑
  - 如果要想通过 bean 的注入，并生成 queue exchange ... 要在 consumer 
服务里注入，否者生成不了





