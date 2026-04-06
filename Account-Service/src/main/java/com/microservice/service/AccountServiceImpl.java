package com.microservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.entity.Account;
import com.microservice.repository.AccountRepository;
import com.microservice.dto.*;
import com.microservice.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final WebClient webClient;

    private final String CUSTOMER_URL =
            "http://localhost:9091/api/customers/exists/";

    // CREATE ACCOUNT
    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO dto){

        log.info("Creating account for customer {}", dto.getCustomerId());

        // Check customer exists
        Boolean exists = webClient.get()
                .uri(CUSTOMER_URL + dto.getCustomerId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(!exists){
            throw new RuntimeException("Customer not found");
        }

        // Check account type already exists
          List<Account> accounts =
                repository.findByCustomerId(dto.getCustomerId());

        boolean alreadyExists = accounts.stream()
                .anyMatch(a ->
                        a.getAccountType()
                                .equalsIgnoreCase(dto.getAccountType()));

        if(alreadyExists){
            throw new RuntimeException(
                    "Customer already has " +
                            dto.getAccountType() + " account");
        }

        Account account = new Account();

        account.setCustomerId(dto.getCustomerId());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        account.setAccountNumber(generateAccountNumber());
        account.setStatus("ACTIVE");

        Account saved = repository.save(account);

        log.info("Account created {}", saved.getAccountNumber());

        return map(saved);
    }

    // GENERATE ACCOUNT NUMBER
    private String generateAccountNumber(){

        long count = repository.count() + 1;

        return String.format("ACC%06d", count);
    }

    // GET ACCOUNT
    @Override
    public AccountResponseDTO getAccountByNumber(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        return map(account);
    }

    // CUSTOMER ACCOUNTS
    @Override
    public List<AccountResponseDTO> getCustomerAccounts(Long customerId){

        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    // ALL ACCOUNTS
    @Override
    public List<AccountResponseDTO> getAllAccounts(){

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // HOLD ACCOUNT
    @Override
    public void holdAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        account.setStatus("HOLD");

        repository.save(account);
    }

    // UNHOLD ACCOUNT
    @Override
    public void unholdAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        account.setStatus("ACTIVE");

        repository.save(account);
    }

    // BLOCK ACCOUNT
    @Override
    public void blockAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        account.setStatus("BLOCK");

        repository.save(account);
    }

    // CREDIT / DEBIT
    @Override
    public Double processTransaction(String accountNumber,
                                     Double amount,
                                     String type){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account not found"));

        double balance = account.getBalance();

        if(type.equalsIgnoreCase("DEBIT")){

            if(balance < amount){
                throw new RuntimeException("Insufficient balance");
            }

            balance -= amount;
        }

        if(type.equalsIgnoreCase("CREDIT")){
            balance += amount;
        }

        account.setBalance(balance);

        repository.save(account);

        log.info("Transaction successful. New balance {}", balance);

        return balance;
    }

    // MAP ENTITY TO DTO
    private AccountResponseDTO map(Account account){

        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());

        return dto;
    }
}