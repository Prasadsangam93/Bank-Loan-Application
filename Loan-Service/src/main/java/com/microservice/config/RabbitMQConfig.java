package com.microservice.config;

import org.springframework.amqp.core.Queue;

import org.springframework.amqp.support.converter.
        Jackson2JsonMessageConverter;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE =
            "loan_notification_queue";

    @Bean
    public Queue queue() {

        return new Queue(QUEUE);
    }

    @Bean
    public Jackson2JsonMessageConverter
    messageConverter() {

        return new Jackson2JsonMessageConverter();
    }
}