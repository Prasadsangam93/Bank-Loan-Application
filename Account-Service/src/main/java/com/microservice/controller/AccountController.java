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

    // CREATE ACCOUNT
    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> create(
            @Valid @RequestBody AccountRequestDTO dto){

        log.info("Create account request for customer {}", dto.getCustomerId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createAccount(dto));
    }

    // GET ACCOUNT BY ACCOUNT NUMBER
    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getAccount(
            @PathVariable String accountNumber){

        log.info("Fetching account {}", accountNumber);

        return ResponseEntity.ok(
                service.getAccountByNumber(accountNumber));
    }

    // GET ACCOUNTS BY CUSTOMER
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDTO>> getCustomerAccounts(
            @PathVariable Long customerId){

        log.info("Fetching accounts for customer {}", customerId);

        return ResponseEntity.ok(
                service.getCustomerAccounts(customerId));
    }

    // GET ALL ACCOUNTS
    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<AccountResponseDTO>> getAll(){

        log.info("Fetching all accounts");

        return ResponseEntity.ok(service.getAllAccounts());
    }

    // HOLD ACCOUNT
    @PutMapping("/hold/{accountNumber}")
    public ResponseEntity<String> hold(@PathVariable String accountNumber){

        log.info("Holding account {}", accountNumber);

        service.holdAccount(accountNumber);

        return ResponseEntity.ok("Account on HOLD");
    }

    // UNHOLD ACCOUNT
    @PutMapping("/unhold/{accountNumber}")
    public ResponseEntity<String> unhold(@PathVariable String accountNumber){

        log.info("Activating account {}", accountNumber);

        service.unholdAccount(accountNumber);

        return ResponseEntity.ok("Account activated");
    }

    // BLOCK ACCOUNT
    @PutMapping("/block/{accountNumber}")
    public ResponseEntity<String> block(@PathVariable String accountNumber){

        log.info("Blocking account {}", accountNumber);

        service.blockAccount(accountNumber);

        return ResponseEntity.ok("Account blocked");
    }

    // CREDIT / DEBIT TRANSACTION
    @PutMapping("/transaction")
    public ResponseEntity<Double> transaction(
            @RequestParam String accountNumber,
            @RequestParam Double amount,
            @RequestParam String type){

        log.info("Transaction request {} for {}", type, accountNumber);

        Double balance =
                service.processTransaction(accountNumber, amount, type);

        return ResponseEntity.ok(balance);
    }
}