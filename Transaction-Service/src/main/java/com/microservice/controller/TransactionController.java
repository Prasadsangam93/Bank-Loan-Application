package com.microservice.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import com.microservice.dto.*;
import com.microservice.service.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/create")
    public ResponseEntity<TransactionResponseDTO> create(
            @RequestBody TransactionRequestDTO dto) throws MessagingException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createTransaction(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDTO>> getByAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                service.getTransactionsByAccount(accountNumber));
    }
}