package com.microservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${twilio.account.sid}")
    private String sid;

    @Value("${twilio.auth.token}")
    private String token;

    @Value("${twilio.phone.number}")
    private String from;

    public void sendSms(String mobile,
                        String accountNumber,
                        String type,
                        Double amount,
                        Double balance){

        try {
            log.info("Sending SMS to {}", mobile);

            Twilio.init(sid, token);

            String lastFour =
                    accountNumber.substring(accountNumber.length() - 4);

            String message =
                    "SmartBank XXXX" + lastFour +
                            " " + type +
                            " ₹" + amount +
                            " Bal:" + balance;

            Message msg = Message.creator(
                    new PhoneNumber(mobile),
                    new PhoneNumber(from),
                    message
            ).create();

            log.info("SMS SENT SUCCESS SID: {}", msg.getSid());

        } catch (Exception e){
            log.error("SMS FAILED", e);
        }
    }
}