package com.microservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public TransactionResponseDTO create(
            @RequestBody TransactionRequestDTO dto){

        return service.createTransaction(dto);
    }

    @GetMapping("/getAllTransactions")
    public List<TransactionResponseDTO> getAll(){

        return service.getAllTransactions();
    }

}