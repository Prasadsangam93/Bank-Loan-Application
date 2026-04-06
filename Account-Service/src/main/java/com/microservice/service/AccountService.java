package com.microservice.service;

import com.microservice.dto.*;

import java.util.List;

public interface AccountService {

    AccountResponseDTO createAccount(AccountRequestDTO dto);

    AccountResponseDTO getAccountByNumber(String accountNumber);

    List<AccountResponseDTO> getCustomerAccounts(Long customerId);

    // NEW METHOD
    List<AccountResponseDTO> getAllAccounts();

    void holdAccount(String accountNumber);

    void unholdAccount(String accountNumber);

    void blockAccount(String accountNumber);
}