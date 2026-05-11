package com.microservice.service;

import com.microservice.dto.*;

import java.util.List;

public interface LoanService {

    LoanResponseDTO applyLoan(
            LoanRequestDTO dto
    );

    LoanResponseDTO approveLoan(
            Long id
    );

    LoanResponseDTO rejectLoan(
            Long id
    );

    LoanResponseDTO closeLoan(
            Long id
    );

    List<LoanResponseDTO> getAllLoans();
}