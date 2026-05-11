package com.microservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanResponseDTO {

    private Long id;

    private String accountNumber;

    private String loanType;

    private Double loanAmount;

    private Double interestRate;

    private Integer tenureMonths;

    private Double emiAmount;

    private Double totalAmount;

    private String status;

    private LocalDate appliedDate;

    private LocalDate approvedDate;

    private LocalDate closedDate;
}