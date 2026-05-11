package com.microservice.dto;

import lombok.Data;

@Data
public class CustomerResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;

    private String email;     // 🔥 used for EmailService
    private String mobile;    // 🔥 used for SmsService
}