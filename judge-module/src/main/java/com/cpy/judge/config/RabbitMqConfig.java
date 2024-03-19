package com.cpy.judge.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq配置类
 *
 * @Author:成希德
 */
@Configuration
public class RabbitMqConfig {
    /**
     * 配置交换机
     * @return
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("judge.code");
    }
    /**
     * 配置队列
     */
    @Bean
    public Queue codeQueue(){
        return new Queue("judge.queue");
    }
    /**
     * 队列绑定交换机
     */
    @Bean
    public Binding bindingQueue(Queue queue, DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("code");
    }
}
