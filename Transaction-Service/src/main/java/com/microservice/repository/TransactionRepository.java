package com.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction,Long> {
}