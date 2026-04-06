package com.microservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendTransactionEmail(String email,
                                     String accountNumber,
                                     String type,
                                     Double amount,
                                     Double balance){

        String lastFour =
                accountNumber.substring(accountNumber.length()-4);

        String message =
                "Dear Customer,\n\n"+
                        "Your account XXXX"+lastFour+
                        " has been "+type+
                        " with ₹"+amount+
                        "\nAvailable Balance: ₹"+balance+
                        "\n\nThank you\nSmart Bank";

        SimpleMailMessage mail=new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("SMART BANK TRANSACTION ALERT");
        mail.setText(message);

        mailSender.send(mail);
    }
}