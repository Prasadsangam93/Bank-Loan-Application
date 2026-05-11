package com.microservice.controller;

import com.microservice.dto.*;
import com.microservice.service.LoanService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService service;

    @PostMapping("/apply")
    public LoanResponseDTO apply(
            @RequestBody LoanRequestDTO dto
    ) {

        return service.applyLoan(dto);
    }

    @PutMapping("/approve/{id}")
    public LoanResponseDTO approve(
            @PathVariable Long id
    ) {

        return service.approveLoan(id);
    }

    @PutMapping("/reject/{id}")
    public LoanResponseDTO reject(
            @PathVariable Long id
    ) {

        return service.rejectLoan(id);
    }

    @PutMapping("/close/{id}")
    public LoanResponseDTO close(
            @PathVariable Long id
    ) {

        return service.closeLoan(id);
    }

    @GetMapping
    public List<LoanResponseDTO> getAll() {

        return service.getAllLoans();
    }
}