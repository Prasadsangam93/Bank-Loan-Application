package com.microservice.dto;

import lombok.Data;

@Data
public class LoanMessageDTO {

    private String email;

    private String mobile;

    private String accountNumber;

    private String type;

    private Double amount;

    private Double balance;
}