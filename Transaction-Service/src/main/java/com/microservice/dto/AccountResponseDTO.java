package com.microservice.dto;

import lombok.Data;

@Data
public class AccountResponseDTO {

    private Long id;

    private Long customerId;

    private String accountNumber;

    private Double balance;
}