package com.microservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionResponseDTO {

    private Long id;
    private String accountNumber;
    private Long customerId;
    private String transactionType;
    private Double amount;
    private LocalDateTime transactionDate;

}