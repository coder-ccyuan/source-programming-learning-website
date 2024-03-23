# source-programming-learning-website
一站式‘源编程学习站’

## 开发日志
___
### 2.22
- 一个坑
  - 如果@SpringBootApplication和@ComponentScan注解共存，那么@SpringBootApplication注解的扫描的作用将会失效，也就是说不能够扫描启动类所在包以及子包了。因此，我们必须在@ComponentScan注解配置本工程需要扫描的包范围。

### 2.23
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
