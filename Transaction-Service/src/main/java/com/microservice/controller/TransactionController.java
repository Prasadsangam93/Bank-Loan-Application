package com.microservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import jakarta.validation.Valid;

import com.microservice.dto.*;
import com.microservice.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class TransactionController {

    private final TransactionService service;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDTO> create(
            @Valid @RequestBody TransactionRequestDTO dto) {

        log.info("Transaction API called");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTransaction(dto));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {

        return ResponseEntity.ok(service.getAllTransactions());
    }

    // GET BY ACCOUNT
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>> getByAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.getTransactionsByAccount(accountNumber));
    }
}