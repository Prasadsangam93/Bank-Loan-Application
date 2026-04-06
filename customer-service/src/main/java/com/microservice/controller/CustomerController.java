package com.microservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import jakarta.validation.Valid;

import com.microservice.service.CustomerService;
import com.microservice.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDTO> register(
            @Valid @RequestBody CustomerRequestDTO dto) {

        log.info("API request received to register customer");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.register(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> get(@PathVariable Long id) {

        log.info("API request received to fetch customer {}", id);

        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {

        log.info("API request received to fetch all customers");

        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO dto) {

        log.info("API request received to update customer {}", id);

        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        log.info("API request received to delete customer {}", id);

        service.delete(id);

        return ResponseEntity.ok("Customer deleted successfully");
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {

        return ResponseEntity.ok(service.existsById(id));
    }
}