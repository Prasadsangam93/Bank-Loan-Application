package com.microservice.service;

import com.microservice.dto.AccountRequestDTO;
import com.microservice.dto.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    // Create account
    AccountResponseDTO createAccount(AccountRequestDTO dto);

    // Get account using account number
    AccountResponseDTO getAccountByNumber(String accountNumber);

    // Get accounts by customer id
    List<AccountResponseDTO> getCustomerAccounts(Long customerId);

    // Get all accounts
    List<AccountResponseDTO> getAllAccounts();

    // Hold account
    void holdAccount(String accountNumber);

    // Activate account
    void unholdAccount(String accountNumber);

    // Block account
    void blockAccount(String accountNumber);

    // Credit / Debit transaction
    Double processTransaction(String accountNumber,
                              Double amount,
                              String type);
}