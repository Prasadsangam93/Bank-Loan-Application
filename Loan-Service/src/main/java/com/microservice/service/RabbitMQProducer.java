package com.microservice.service;

import com.microservice.config.RabbitMQConfig;

import com.microservice.dto.LoanMessageDTO;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.
        RabbitTemplate;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(
            LoanMessageDTO dto
    ) {

        log.info(
                "Sending RabbitMQ Message {}",
                dto
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.QUEUE,
                dto
        );

        System.out.println(
                "MESSAGE SENT"
        );
    }
}