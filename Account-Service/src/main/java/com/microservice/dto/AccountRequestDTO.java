package com.microservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountRequestDTO {

    @NotNull(message="Customer ID required")
    private Long customerId;

    @NotBlank(message="Account type required")
    private String accountType;

    @Positive(message="Balance must be greater than 0")
    private Double balance;
}