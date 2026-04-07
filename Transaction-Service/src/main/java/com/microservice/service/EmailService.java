package com.microservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendTransactionEmail(
            String email,
            String accountNumber,
            String type,
            Double amount,
            Double balance) throws Exception {

        String lastFour =
                accountNumber.substring(accountNumber.length()-4);

        Context context = new Context();
        context.setVariable("lastFour", lastFour);
        context.setVariable("type", type);
        context.setVariable("amount", amount);
        context.setVariable("balance", balance);

        String html =
                templateEngine.process("transaction-email", context);

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("SMART BANK TRANSACTION ALERT");
        helper.setText(html, true);

        mailSender.send(message);
    }
}