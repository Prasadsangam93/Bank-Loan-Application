package com.microservice.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {

    private String accountNumber;
    private String email;
    private Long customerId;
    private Double balance;
}