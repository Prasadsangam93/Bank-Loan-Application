package com.microservice.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class CustomerRequestDTO {

    @NotBlank(message = "First Name is required")
    @Size(min = 3, max = 30,
            message = "First Name must be 3 to 30 characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 3, max = 30,
            message = "Last Name must be 3 to 30 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email format")
    private String email;

    @NotBlank(message = "Mobile is required")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Mobile must be exactly 10 digits")
    private String mobile;

}