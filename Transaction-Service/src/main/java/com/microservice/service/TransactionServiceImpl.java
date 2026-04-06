package com.microservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservice.dto.*;
import com.microservice.entity.Transaction;
import com.microservice.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final WebClient webClient;
    private final EmailService emailService;

    private final String ACCOUNT_URL =
            "http://localhost:9092/api/accounts/";

    private final String CUSTOMER_URL =
            "http://localhost:9091/api/customers/";

    @Override
    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto){

        // 1️⃣ Call Account Service to update balance
        Double newBalance = webClient.put()
                .uri(ACCOUNT_URL + "transaction?accountNumber="
                        + dto.getAccountNumber()
                        + "&amount=" + dto.getAmount()
                        + "&type=" + dto.getTransactionType())
                .retrieve()
                .bodyToMono(Double.class)
                .block();

        // 2️⃣ Get customer email from Customer Service
        CustomerResponseDTO customer = webClient.get()
                .uri(CUSTOMER_URL + dto.getCustomerId())
                .retrieve()
                .bodyToMono(CustomerResponseDTO.class)
                .block();

        // 3️⃣ Save transaction
        Transaction transaction = new Transaction();

        transaction.setAccountNumber(dto.getAccountNumber());
        transaction.setCustomerId(dto.getCustomerId());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction saved = repository.save(transaction);

        // 4️⃣ Send email notification
        emailService.sendTransactionEmail(
                customer.getEmail(),
                dto.getAccountNumber(),
                dto.getTransactionType(),
                dto.getAmount(),
                newBalance
        );

        // 5️⃣ Response DTO
        TransactionResponseDTO response = new TransactionResponseDTO();

        response.setId(saved.getId());
        response.setAccountNumber(saved.getAccountNumber());
        response.setCustomerId(saved.getCustomerId());
        response.setTransactionType(saved.getTransactionType());
        response.setAmount(saved.getAmount());
        response.setTransactionDate(saved.getTransactionDate());

        return response;
    }

    @Override
    public List<TransactionResponseDTO> getAllTransactions(){

        return repository.findAll()
                .stream()
                .map(t -> {

                    TransactionResponseDTO dto =
                            new TransactionResponseDTO();

                    dto.setId(t.getId());
                    dto.setAccountNumber(t.getAccountNumber());
                    dto.setCustomerId(t.getCustomerId());
                    dto.setTransactionType(t.getTransactionType());
                    dto.setAmount(t.getAmount());
                    dto.setTransactionDate(t.getTransactionDate());

                    return dto;

                }).toList();
    }

}