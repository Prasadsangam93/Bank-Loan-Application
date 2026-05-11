package com.microservice.service;

import com.microservice.dto.*;

import com.microservice.entity.Loan;

import com.microservice.repository.LoanRepository;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanServiceImpl
        implements LoanService {

    private final LoanRepository repository;

    private final WebClient webClient;

    private final RabbitMQProducer producer;

    // ================= APPLY =================

    @Override
    public LoanResponseDTO applyLoan(
            LoanRequestDTO dto
    ) {

        log.info(
                "Loan apply started {}",
                dto.getAccountNumber()
        );

        // ================= ACCOUNT SERVICE =================

        AccountResponseDTO account =
                webClient.get()

                        .uri(
                                "http://localhost:9092/api/accounts/accountNumber/"
                                        + dto.getAccountNumber()
                        )

                        .retrieve()

                        .bodyToMono(
                                AccountResponseDTO.class
                        )

                        .block();

        if(account == null) {

            throw new RuntimeException(
                    "Account not found"
            );
        }

        // ================= CUSTOMER SERVICE =================

        CustomerResponseDTO customer =
                webClient.get()

                        .uri(
                                "http://localhost:9091/api/customers/"
                                        + account.getCustomerId()
                        )

                        .retrieve()

                        .bodyToMono(
                                CustomerResponseDTO.class
                        )

                        .block();

        // ================= SAVE LOAN =================

        Loan loan = new Loan();

        loan.setAccountNumber(
                dto.getAccountNumber()
        );

        loan.setLoanType(
                dto.getLoanType()
        );

        loan.setLoanAmount(
                dto.getLoanAmount()
        );

        loan.setInterestRate(
                dto.getInterestRate()
        );

        loan.setTenureMonths(
                dto.getTenureMonths()
        );

        double emi =
                dto.getLoanAmount() * 0.01;

        loan.setEmiAmount(emi);

        loan.setTotalAmount(
                emi * dto.getTenureMonths()
        );

        Loan saved =
                repository.save(loan);

        // ================= RABBITMQ =================

        LoanMessageDTO msg =
                new LoanMessageDTO();

        msg.setEmail(
                customer.getEmail()
        );

        msg.setMobile(
                customer.getMobile()
        );

        msg.setAccountNumber(
                loan.getAccountNumber()
        );

        msg.setType(
                "LOAN APPLIED"
        );

        msg.setAmount(
                loan.getLoanAmount()
        );

        msg.setBalance(
                loan.getTotalAmount()
        );

        producer.sendNotification(msg);

        return map(saved);
    }

    // ================= APPROVE =================

    @Override
    public LoanResponseDTO approveLoan(
            Long id
    ) {

        Loan loan =
                repository.findById(id)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found"
                                ));

        loan.setStatus("APPROVED");

        loan.setApprovedDate(
                LocalDate.now()
        );

        loan.setNextEmiDate(
                LocalDate.now().plusMonths(1)
        );

        Loan saved =
                repository.save(loan);

        return map(saved);
    }

    // ================= REJECT =================

    @Override
    public LoanResponseDTO rejectLoan(
            Long id
    ) {

        Loan loan =
                repository.findById(id)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found"
                                ));

        loan.setStatus("REJECTED");

        Loan saved =
                repository.save(loan);

        return map(saved);
    }

    // ================= CLOSE =================

    @Override
    public LoanResponseDTO closeLoan(
            Long id
    ) {

        Loan loan =
                repository.findById(id)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found"
                                ));

        loan.setStatus("CLOSED");

        loan.setClosedDate(
                LocalDate.now()
        );

        Loan saved =
                repository.save(loan);

        return map(saved);
    }

    // ================= GET ALL =================

    @Override
    public List<LoanResponseDTO>
    getAllLoans() {

        return repository.findAll()

                .stream()

                .map(this::map)

                .toList();
    }

    // ================= MAPPER =================

    private LoanResponseDTO map(
            Loan loan
    ) {

        LoanResponseDTO dto =
                new LoanResponseDTO();

        dto.setId(
                loan.getId()
        );

        dto.setAccountNumber(
                loan.getAccountNumber()
        );

        dto.setLoanType(
                loan.getLoanType()
        );

        dto.setLoanAmount(
                loan.getLoanAmount()
        );

        dto.setInterestRate(
                loan.getInterestRate()
        );

        dto.setTenureMonths(
                loan.getTenureMonths()
        );

        dto.setEmiAmount(
                loan.getEmiAmount()
        );

        dto.setTotalAmount(
                loan.getTotalAmount()
        );

        dto.setStatus(
                loan.getStatus()
        );

        dto.setAppliedDate(
                loan.getAppliedDate()
        );

        dto.setApprovedDate(
                loan.getApprovedDate()
        );

        dto.setClosedDate(
                loan.getClosedDate()
        );

        return dto;
    }
}