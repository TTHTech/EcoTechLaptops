package org.example.test.service;

import org.example.test.model.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerServiceRegister extends UserDetailsService {
    Customer save(Customer customer);
}
