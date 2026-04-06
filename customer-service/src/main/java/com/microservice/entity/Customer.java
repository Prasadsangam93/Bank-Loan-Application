package com.microservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    private String status;

    private String kycStatus;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        this.status = "ACTIVE";
        this.kycStatus = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}