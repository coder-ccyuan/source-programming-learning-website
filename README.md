# source-programming-learning-website
一站式‘源编程学习站’

- 一个坑
  - 如果@SpringBootApplication和@ComponentScan注解共存，那么@SpringBootApplication注解的扫描的作用将会失效，也就是说不能够扫描启动类所在包以及子包了。因此，我们必须在@ComponentScan注解配置本工程需要扫描的包范围。
