package com.microservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.microservice.dto.*;
import com.microservice.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {

    private final AccountService service;

    @PostMapping("/create")
    public AccountResponseDTO create(@RequestBody AccountRequestDTO dto){
        return service.createAccount(dto);
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public AccountResponseDTO getByAccountNumber(
            @PathVariable String accountNumber){

        return service.getAccountByNumber(accountNumber);
    }

    @GetMapping("/customer/{customerId}")
    public List<AccountResponseDTO> getCustomerAccounts(
            @PathVariable Long customerId){

        return service.getCustomerAccounts(customerId);
    }

    // NEW API
    @GetMapping("/getAllAccounts")
    public List<AccountResponseDTO> getAllAccounts(){
        return service.getAllAccounts();
    }

    @PutMapping("/hold/{accountNumber}")
    public String hold(@PathVariable String accountNumber){

        service.holdAccount(accountNumber);

        return "Account on HOLD";
    }

    @PutMapping("/unhold/{accountNumber}")
    public String unhold(@PathVariable String accountNumber){

        service.unholdAccount(accountNumber);

        return "Account activated";
    }

    @PutMapping("/block/{accountNumber}")
    public String block(@PathVariable String accountNumber){

        service.blockAccount(accountNumber);

        return "Account blocked";
    }
}