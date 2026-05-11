package com.microservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String loanType;
    private Double loanAmount;
    private Double interestRate;
    private Integer tenureMonths;

    private Double emiAmount;
    private Double totalAmount;

    private String status = "PENDING";

    private LocalDate appliedDate = LocalDate.now();
    private LocalDate approvedDate;
    private LocalDate closedDate;

    // ADD THIS (VERY IMPORTANT)
    private LocalDate nextEmiDate;
}