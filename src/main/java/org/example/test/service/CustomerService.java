package org.example.test.service;

import java.util.List;

import org.example.test.model.Customer;
import org.example.test.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomer() {
        return this.customerRepository.findAll();
    }

}
