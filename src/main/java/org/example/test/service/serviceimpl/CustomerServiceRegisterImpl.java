package org.example.test.service.serviceimpl;


import org.example.test.model.Customer;
import org.example.test.repository.CustomerRepository;
import org.example.test.service.CustomerServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceRegisterImpl implements CustomerServiceRegister {

    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public CustomerServiceRegisterImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomer != null) {
            throw new IllegalStateException("Email đã được đăng ký");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreateDate(new java.util.Date());
        customer.setUpdateDate(new java.util.Date());

        return customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username);
        if (customer == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(customer.getEmail(), customer.getPassword(), java.util.Collections.emptyList());
    }
}
