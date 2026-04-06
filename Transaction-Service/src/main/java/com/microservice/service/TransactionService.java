package com.microservice.service;

import com.microservice.dto.*;

import java.util.List;

public interface TransactionService {

    TransactionResponseDTO createTransaction(TransactionRequestDTO dto);

    List<TransactionResponseDTO> getAllTransactions();

}