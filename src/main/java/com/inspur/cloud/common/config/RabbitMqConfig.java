package com.inspur.cloud.common.config;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: JiangYP
 * @Date: 2020/8/20 9:20
 */

@Configuration
public class RabbitMqConfig {
    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate){
        //设置消息序列化方式
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return new RabbitMessagingTemplate(rabbitTemplate);
    }
}
