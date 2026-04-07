package com.microservice.dto;

import lombok.Data;

@Data
public class TransactionRequestDTO {

    private String accountNumber;
    private String transactionType;
    private Double amount;
}