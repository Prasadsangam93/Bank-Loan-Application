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
    private final EmailService emailService;
    private  final  SmsService smsService;

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {

        log.info("Transaction started {}", dto.getAccountNumber());

        // 1. CALL ACCOUNT SERVICE (GET BALANCE + CUSTOMERID)
        AccountResponseDTO account =
                webClient.put()
                        .uri("http://localhost:9092/api/accounts/transaction?accountNumber="
                                + dto.getAccountNumber()
                                + "&amount=" + dto.getAmount()
                                + "&type=" + dto.getTransactionType())
                        .retrieve()
                        .bodyToMono(AccountResponseDTO.class)
                        .block();

        Double updatedBalance = account.getBalance();
        Long customerId = account.getCustomerId();

        log.info("CustomerId {}", customerId);

        // 2. CALL CUSTOMER SERVICE (GET EMAIL)
        CustomerResponseDTO customer =
                webClient.get()
                        .uri("http://localhost:9091/api/customers/" + customerId)
                        .retrieve()
                        .bodyToMono(CustomerResponseDTO.class)
                        .block();

        String email = customer.getEmail();

        log.info("Customer email {}", email);

        // 3. SAVE TRANSACTION
        Transaction t = new Transaction();
        t.setAccountNumber(dto.getAccountNumber());
        t.setTransactionType(dto.getTransactionType());
        t.setAmount(dto.getAmount());
        t.setTransactionDate(LocalDateTime.now());

        Transaction saved = repository.save(t);

        // 4. SEND EMAIL
        try {
            emailService.sendTransactionEmail(
                    email,
                    dto.getAccountNumber(),
                    dto.getTransactionType(),
                    dto.getAmount(),
                    updatedBalance
            );
            smsService.sendSms(
                    customer.getMobile(),
                    dto.getAccountNumber(),
                    dto.getTransactionType(),
                    dto.getAmount(),
                    updatedBalance
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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