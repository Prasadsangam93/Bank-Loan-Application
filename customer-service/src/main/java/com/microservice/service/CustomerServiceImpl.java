package com.microservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.microservice.repository.CustomerRepository;
import com.microservice.entity.Customer;
import com.microservice.dto.*;
import com.microservice.exception.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public CustomerResponseDTO register(CustomerRequestDTO dto) {

        if(repository.existsByEmail(dto.getEmail()))
            throw new DuplicateResourceException("Email already exists");

        if(repository.existsByMobile(dto.getMobile()))
            throw new DuplicateResourceException("Mobile already exists");

        Customer customer=new Customer();

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setMobile(dto.getMobile());

        Customer saved=repository.save(customer);

        return map(saved);
    }

    @Override
    public CustomerResponseDTO getById(Long id){

        Customer customer=repository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return map(customer);
    }

    @Override
    public List<CustomerResponseDTO> getAll(){

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public CustomerResponseDTO update(Long id,CustomerRequestDTO dto){

        Customer customer=repository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setMobile(dto.getMobile());

        Customer updated=repository.save(customer);

        return map(updated);
    }

    @Override
    public void delete(Long id){

        if(!repository.existsById(id))
            throw new CustomerNotFoundException("Customer not found");

        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id){
        return repository.existsById(id);
    }

    private CustomerResponseDTO map(Customer c){

        CustomerResponseDTO dto=new CustomerResponseDTO();

        dto.setId(c.getId());
        dto.setFirstName(c.getFirstName());
        dto.setLastName(c.getLastName());
        dto.setEmail(c.getEmail());
        dto.setMobile(c.getMobile());
        dto.setStatus(c.getStatus());
        dto.setKycStatus(c.getKycStatus());
        dto.setCreatedAt(c.getCreatedAt());

        return dto;
    }
}