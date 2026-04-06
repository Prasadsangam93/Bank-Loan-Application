package com.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{

    // FETCH BASED ON ACCOUNT NUMBER
    List<Transaction> findByAccountNumber(String accountNumber);

}