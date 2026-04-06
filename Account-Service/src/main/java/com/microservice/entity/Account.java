package com.microservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="accounts",
        uniqueConstraints=@UniqueConstraint(
                columnNames={"customerId","accountType"}))
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,length=10)
    private String accountNumber;

    private Long customerId;

    private String accountType;

    private Double balance;

    private String status="ACTIVE";
}