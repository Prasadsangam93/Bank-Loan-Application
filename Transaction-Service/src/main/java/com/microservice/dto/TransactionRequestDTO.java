package com.microservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TransactionRequestDTO {

    @NotBlank(message="Account number required")
    private String accountNumber;

    @NotBlank(message="Transaction type required")
    private String transactionType;

    @Positive(message="Amount must be greater than 0")
    private Double amount;
}