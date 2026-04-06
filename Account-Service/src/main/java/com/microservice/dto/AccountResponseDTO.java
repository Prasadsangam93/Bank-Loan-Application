package com.microservice.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {

    private Long id;
    private Long customerId;
    private String accountNumber;
    private String accountType;
    private Double balance;
    private String status;
}