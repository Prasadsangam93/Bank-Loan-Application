package com.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.entity.Account;

import java.util.Optional;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long>{

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(Long customerId);

    boolean existsByCustomerIdAndAccountType(
            Long customerId,
            String accountType);
}