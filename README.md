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
  没有context-path的话，无法区分
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




