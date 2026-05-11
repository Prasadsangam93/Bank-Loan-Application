package com.microservice.dto;

import lombok.Data;

@Data
public class LoanRequestDTO {

    private String accountNumber;

    private String loanType;

    private Double loanAmount;

    private Double interestRate;

    private Integer tenureMonths;
}