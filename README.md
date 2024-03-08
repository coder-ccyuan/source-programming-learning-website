# source-programming-learning-website(一站式‘源编程开发学习站’) version 2.0  

*source* 是一站式编程开发学习平台 有两大模块组成**算法学习模块**和**API接口开放平台模块**
组成 。
- **算法学习模块** 主要是提供算法题集，用户通过刷算法题提升算法能力
- **API接口开放平台模块** 提供辅助开发的API，辅助开发者进行开发
___

## version 2.0 做了什么？

--- 

1.将version 1.0 升级为分布式  

2.分布式技术选型    
- Spring Cloud Alibaba
  - Nacos 作为服务发现&注册中心&配置中心
- Open Feign 远程调用
- Spring Gateway 内部网关
- RabbitMQ 消息队列
- redis 分布式session 缓存

___

## 开发日志

### 3.15
- 创建User 模块 √
- 将单体项目拆分模块 √
   - User 模块
   - OJ 模块
   - API 模块
   - API-gateway 模块
   - OJ-sandBox 模块
   - common 模块
- 将通用的model 放入 common模块中 √
- 将通用返回类,工具类，常量，异常处理类放入common模块 √
- API module 导入common的包 √
- OJ module 重新导入common的包 √
- 增加Spring Cloud Alibaba 依赖 ，并配置相关信息 √
- 增加Open feign 依赖并配置相关信息 √
- 开发feignClient 客户端 实现远程调用

---

### 3.16
- 继续开发feignClient 




