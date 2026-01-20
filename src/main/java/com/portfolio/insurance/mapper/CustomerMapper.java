package com.portfolio.insurance.mapper;

import com.portfolio.insurance.domain.Customer;
import com.portfolio.insurance.dto.CustomerRequest;
import com.portfolio.insurance.dto.CustomerResponse;

public class CustomerMapper {

    public static Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.fullName());
        customer.setCpf(request.cpf());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setBirthDate(request.birthDate());
        return customer;
    }

    public static void updateEntity(Customer customer, CustomerRequest request) {
        customer.setFullName(request.fullName());
        customer.setCpf(request.cpf());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setBirthDate(request.birthDate());
    }

    public static CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getCpf(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getBirthDate(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
