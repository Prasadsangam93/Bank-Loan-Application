package com.microservice.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {

    private Long id;
    private String accountNumber;
    private Long customerId;
    private String accountType;
    private Double balance;
    private String status;
}