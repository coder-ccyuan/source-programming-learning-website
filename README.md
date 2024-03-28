# source-programming-learning-website
一站式‘源编程学习站’

## 开发日志
___
### 3.22
- 一个坑
  - 如果@SpringBootApplication和@ComponentScan注解共存，那么@SpringBootApplication注解的扫描的作用将会失效，也就是说不能够扫描启动类所在包以及子包了。因此，我们必须在@ComponentScan注解配置本工程需要扫描的包范围。

### 3.23
- 实现api-gateway 返回类的统一 
- 将 api-gateway 使用的url统一抽离出作为静态常量
- 完善 api-gateway 远程调用验证响应是否符合要求
- 遇到一个坑
  - 如果涉及到泛型对象与 json 互转，无法直接使用class对象完成转换
  - ```
      import cn.hutool.core.lang.TypeReference;
      import cn.hutool.json.JSONUtil;
      public void test(){
      Result<User> result= new Result<User>();
      User user = new User();
      user.setId("1");
      user.setName("name");
      result.setData(user);
      //-- 正常情况
      //对象转json
      String str = JSONObject.toJSONString(result);
      System.out.println(str);
      //json str 转对象
      Result res= JSONObject.parseObject(str,Result .class);
      System.out.println(res);
          
      //--错误情况
      //泛型 不能使用强转，不管用，报错
      Result <User> rest= JSONObject.parseObject(str,Result .class);
      System.out.println(rest);
      System.out.println(rest.getData().getName());
      
      //需要引入hutool包
      //泛型，正常转换
       Result<User> rests2= JSONUtil.toBean(str,new TypeReference<Result<User>>(){},false);
        System.out.println(rests2.getData().getName());
      }
    ```
- 实现题目提交次数和通过次数
- 记录一个坑，maven多模块打包，要在common模块加上
  - ```
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <classifier>exec</classifier>
                </configuration>
            </plugin>
        </plugins>
    </build>
    ```
## 2.26
- 使用redis作为缓存，存储用户信息，题目信息，题目提交信息
  - 配置SpringDataRedis 序列化方式
  - 默认时JDK序列化方式
  - 自定义序列化
  - ```java
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //创建序列化工具
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        //key 和 hashKey 使用string序列化
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //值使用 json做序列化
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setConnectionFactory(factory);
    ```
  - 使用json序列化器会带来额外的花销，所以一般我们会将key和value 都作为 string 存储 所以使用 StringRedisTemplate ,默认是将 key 和 value 使用 string 存储
  - 在存储和更新的时候添加或删除缓存，查询未命中时添加缓存
- 解决缓存穿透问题
- 解决缓存击穿问题
- 解决缓存雪崩问题

### 3.28
- 实现用户信息缓存 :ok:
- 实现题目信息缓存 :ok:
- 实现题目提交信息缓存 :ok:
- 实现api信息缓存 :ok:
- 实现用户Api信息缓存 :ok:
