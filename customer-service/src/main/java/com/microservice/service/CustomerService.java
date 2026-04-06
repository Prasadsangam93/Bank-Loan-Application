package com.microservice.service;

import com.microservice.dto.*;
import java.util.List;

public interface CustomerService {

    CustomerResponseDTO register(CustomerRequestDTO dto);

    CustomerResponseDTO getById(Long id);

    List<CustomerResponseDTO> getAll();

    CustomerResponseDTO update(Long id, CustomerRequestDTO dto);

    void delete(Long id);

    boolean existsById(Long id);
}