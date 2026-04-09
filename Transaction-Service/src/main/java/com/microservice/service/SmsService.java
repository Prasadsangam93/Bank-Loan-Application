package com.microservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void sendSms(String mobile,
                        String accountNumber,
                        String type,
                        Double amount,
                        Double balance){

        Twilio.init(accountSid, authToken);

        String lastFour =
                accountNumber.substring(accountNumber.length()-4);

        String message =
                "SmartBank XXXX"+lastFour+
                        " "+type+
                        " Rs."+amount+
                        " Bal:"+balance;

        Message.creator(
                new PhoneNumber(mobile),
                new PhoneNumber(fromNumber),
                message
        ).create();
    }
}