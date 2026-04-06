package com.microservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AccountRequestDTO {

    @NotNull(message = "Customer Id is required")
    private Long customerId;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @Positive(message = "Balance must be greater than 0")
    private Double balance;
}