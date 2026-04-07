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

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO dto){

        Boolean exists = webClient.get()
                .uri(CUSTOMER_URL + dto.getCustomerId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if(!exists){
            throw new RuntimeException("Customer not found");
        }

        if(repository.existsByCustomerIdAndAccountType(
                dto.getCustomerId(),
                dto.getAccountType())){

            throw new DuplicateAccountException(
                    "Customer already has " +
                            dto.getAccountType() + " account");
        }

        Account account = new Account();

        account.setCustomerId(dto.getCustomerId());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        account.setAccountNumber(generateAccountNumber());
        account.setStatus("ACTIVE");

        return map(repository.save(account));
    }

    private String generateAccountNumber(){
        long count = repository.count()+1;
        return String.format("ACC%06d",count);
    }

    @Override
    public AccountResponseDTO getAccountByNumber(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        return map(account);
    }

    @Override
    public List<AccountResponseDTO> getCustomerAccounts(Long customerId){

        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts(){

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public void holdAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        account.setStatus("HOLD");

        repository.save(account);
    }

    @Override
    public void unholdAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        account.setStatus("ACTIVE");

        repository.save(account);
    }

    @Override
    public void blockAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        account.setStatus("BLOCK");

        repository.save(account);
    }

    // CREDIT / DEBIT
    @Override
    public AccountResponseDTO processTransaction(
            String accountNumber,
            Double amount,
            String type){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found"));

        double balance = account.getBalance();

        if(type.equalsIgnoreCase("DEBIT")){

            if(balance < amount){
                throw new InsufficientBalanceException(
                        "Insufficient balance");
            }

            balance -= amount;
        }

        if(type.equalsIgnoreCase("CREDIT")){
            balance += amount;
        }

        account.setBalance(balance);

        repository.save(account);

        return map(account);
    }

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