package com.microservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.microservice.dto.*;
import com.microservice.entity.Account;
import com.microservice.repository.AccountRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO dto){

        Account account = new Account();

        account.setCustomerId(dto.getCustomerId());
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());
        account.setAccountNumber("ACC" + System.currentTimeMillis());
        account.setStatus("ACTIVE");

        Account saved = repository.save(account);

        return map(saved);
    }

    @Override
    public AccountResponseDTO getAccountByNumber(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return map(account);
    }

    @Override
    public List<AccountResponseDTO> getCustomerAccounts(Long customerId){

        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::map)
                .toList();
    }

    // NEW IMPLEMENTATION
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
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setStatus("HOLD");

        repository.save(account);
    }

    @Override
    public void unholdAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setStatus("ACTIVE");

        repository.save(account);
    }

    @Override
    public void blockAccount(String accountNumber){

        Account account = repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setStatus("BLOCK");

        repository.save(account);
    }

    private AccountResponseDTO map(Account account){

        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setId(account.getId());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());

        return dto;
    }
}