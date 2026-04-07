package com.microservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import jakarta.validation.Valid;

import com.microservice.dto.*;
import com.microservice.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class AccountController {

    private final AccountService service;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> create(
            @Valid @RequestBody AccountRequestDTO dto){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createAccount(dto));
    }

    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getAccount(
            @PathVariable String accountNumber){

        return ResponseEntity.ok(
                service.getAccountByNumber(accountNumber));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDTO>> getCustomerAccounts(
            @PathVariable Long customerId){

        return ResponseEntity.ok(
                service.getCustomerAccounts(customerId));
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<AccountResponseDTO>> getAll(){

        return ResponseEntity.ok(service.getAllAccounts());
    }

    @PutMapping("/hold/{accountNumber}")
    public ResponseEntity<String> hold(@PathVariable String accountNumber){

        service.holdAccount(accountNumber);

        return ResponseEntity.ok("Account on HOLD");
    }

    @PutMapping("/unhold/{accountNumber}")
    public ResponseEntity<String> unhold(@PathVariable String accountNumber){

        service.unholdAccount(accountNumber);

        return ResponseEntity.ok("Account activated");
    }

    @PutMapping("/block/{accountNumber}")
    public ResponseEntity<String> block(@PathVariable String accountNumber){

        service.blockAccount(accountNumber);

        return ResponseEntity.ok("Account blocked");
    }

    // IMPORTANT FIX
    @PutMapping("/transaction")
    public ResponseEntity<AccountResponseDTO> transaction(
            @RequestParam String accountNumber,
            @RequestParam Double amount,
            @RequestParam String type){

        return ResponseEntity.ok(
                service.processTransaction(
                        accountNumber,
                        amount,
                        type));
    }
}