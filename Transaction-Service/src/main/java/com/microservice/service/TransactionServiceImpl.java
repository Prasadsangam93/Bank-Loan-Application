package com.microservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.dto.*;
import com.microservice.entity.Transaction;
import com.microservice.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final WebClient webClient;

    // CREATE TRANSACTION
    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {

        log.info("Transaction started for account {}", dto.getAccountNumber());

        // ✅ VALIDATION
        if (dto.getAccountNumber() == null || dto.getAccountNumber().isBlank()) {
            throw new RuntimeException("Account number required");
        }

        if (!dto.getTransactionType().equalsIgnoreCase("CREDIT") &&
                !dto.getTransactionType().equalsIgnoreCase("DEBIT")) {
            throw new RuntimeException("Invalid transaction type");
        }

        // ✅ CALL ACCOUNT SERVICE (FIXED URL)
        Double updatedBalance = webClient.put()
                .uri("http://localhost:9092/api/accounts/transaction?accountNumber="
                        + dto.getAccountNumber()
                        + "&amount=" + dto.getAmount()
                        + "&type=" + dto.getTransactionType())
                .retrieve()
                .bodyToMono(Double.class)
                .block();

        log.info("Updated balance = {}", updatedBalance);

        // ✅ SAVE TRANSACTION
        Transaction t = new Transaction();

        t.setAccountNumber(dto.getAccountNumber());
        t.setTransactionType(dto.getTransactionType());
        t.setAmount(dto.getAmount());
        t.setTransactionDate(LocalDateTime.now());

        Transaction saved = repository.save(t);

        log.info("Transaction saved with id {}", saved.getId());

        return map(saved);
    }

    // GET ALL
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {

        log.info("Fetching all transactions");

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // GET BY ACCOUNT
    @Override
    public List<TransactionResponseDTO> getTransactionsByAccount(String accountNumber) {

        log.info("Fetching transactions for {}", accountNumber);

        return repository.findByAccountNumber(accountNumber)
                .stream()
                .map(this::map)
                .toList();
    }

    // MAPPING
    private TransactionResponseDTO map(Transaction t) {

        TransactionResponseDTO dto = new TransactionResponseDTO();

        dto.setId(t.getId());
        dto.setAccountNumber(t.getAccountNumber());
        dto.setTransactionType(t.getTransactionType());
        dto.setAmount(t.getAmount());
        dto.setTransactionDate(t.getTransactionDate());

        return dto;
    }
}