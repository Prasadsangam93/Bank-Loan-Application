package com.microservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(String to,
                          String type,
                          String accountNumber,
                          Double amount,
                          Double balance) {

        try {
            log.info("Preparing email for {}", to);

            // last 4 digits
            String lastFour =
                    accountNumber.substring(accountNumber.length() - 4);

            // template variables
            Context context = new Context();
            context.setVariable("type", type);
            context.setVariable("lastFour", lastFour);
            context.setVariable("amount", amount);
            context.setVariable("balance", balance);

            // HTML template (loan-email.html or transaction-email.html)
            String html =
                    templateEngine.process("loan-email", context);

            // create mail
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("Smart Bank Alert");
            helper.setText(html, true);

            mailSender.send(message);

            log.info("EMAIL SENT SUCCESS to {}", to);

        } catch (Exception e) {
            log.error("EMAIL FAILED", e);
        }
    }
}