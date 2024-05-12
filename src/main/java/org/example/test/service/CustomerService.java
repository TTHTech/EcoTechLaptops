package org.example.test.service;

import org.example.test.model.Customer;
import org.example.test.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder; // Lưu trữ tham chiếu tới passwordEncoder
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public List<Customer> getAllCustomer() {
        return this.customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = findById(id);
        if (existingCustomer != null) {
            existingCustomer.setName(updatedCustomer.getName());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setPhone(updatedCustomer.getPhone());
            existingCustomer.setAddress(updatedCustomer.getAddress());
            existingCustomer.setImage(updatedCustomer.getImage());
            return customerRepository.save(existingCustomer);
        }
        return null; // Handle not found case
    }

    // Assume this checks the password as plain text; replace logic as needed
    // public boolean checkPassword(Long customerId, String password) {
    // Customer customer = findById(customerId);
    // return customer != null && customer.getPassword().equals(password);
    // }

    // Update customer's password
    // Phương thức trong CustomerService để thay đổi mật khẩu
    public boolean changePassword(Long customerId, String currentPassword, String newPassword) {
        Customer customer = findById(customerId);
        if (customer != null && passwordEncoder.matches(currentPassword, customer.getPassword())) {
            // Mã hóa mật khẩu mới
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            // Cập nhật mật khẩu
            customer.setPassword(encodedNewPassword);
            customerRepository.save(customer);
            return true;
        }
        return false; // Trả về false nếu mật khẩu hiện tại không đúng hoặc người dùng không tồn tại
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }
}
