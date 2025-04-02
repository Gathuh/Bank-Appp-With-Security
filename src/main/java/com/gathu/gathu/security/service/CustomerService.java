package com.gathu.gathu.security.service;

import com.gathu.gathu.security.dto.CustomerRequest;
import com.gathu.gathu.security.entity.Customer;
import com.gathu.gathu.security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer onboardCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        return customerRepository.save(customer);
    }
}