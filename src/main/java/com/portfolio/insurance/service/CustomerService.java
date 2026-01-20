package com.portfolio.insurance.service;

import com.portfolio.insurance.domain.Customer;
import com.portfolio.insurance.dto.CustomerRequest;
import com.portfolio.insurance.dto.CustomerResponse;
import com.portfolio.insurance.exception.NotFoundException;
import com.portfolio.insurance.mapper.CustomerMapper;
import com.portfolio.insurance.repository.CustomerRepository;
import com.portfolio.insurance.repository.spec.CustomerSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse create(CustomerRequest request) {
        log.info("Criando cliente: {}", request.fullName());
        Customer customer = CustomerMapper.toEntity(request);
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> list(String search, Pageable pageable) {
        Specification<Customer> spec = CustomerSpecifications.nameOrCpfLike(search);
        return customerRepository.findAll(spec, pageable).map(CustomerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CustomerResponse get(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        return CustomerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse update(UUID id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        CustomerMapper.updateEntity(customer, request);
        return CustomerMapper.toResponse(customerRepository.save(customer));
    }

    @Transactional
    public void delete(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        customerRepository.delete(customer);
        log.info("Cliente removido: {}", id);
    }
}
