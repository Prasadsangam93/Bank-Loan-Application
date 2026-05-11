package com.microservice.consumer;

import com.microservice.config.RabbitMQConfig;

import com.microservice.dto.LoanMessageDTO;

import com.microservice.service.EmailService;

import com.microservice.service.SmsService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.
        RabbitListener;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoanConsumer {

    private final EmailService emailService;

    private final SmsService smsService;

    @RabbitListener(
            queues = RabbitMQConfig.QUEUE
    )
    public void consume(
            LoanMessageDTO dto
    ) {

        log.info(
                "Received RabbitMQ Message {}",
                dto
        );

        System.out.println(
                "MESSAGE RECEIVED"
        );

        emailService.sendEmail(
                dto.getEmail(),
                dto.getType(),
                dto.getAccountNumber(),
                dto.getAmount(),
                dto.getBalance()
        );

        smsService.sendSms(
                dto.getMobile(),
                dto.getAccountNumber(),
                dto.getType(),
                dto.getAmount(),
                dto.getBalance()
        );
    }
}