package org.example.test.service;

import org.example.test.model.Admin;
import org.example.test.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminService implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found with email: " + username);
        }
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        return new org.springframework.security.core.userdetails.User(
                admin.getEmail(),
                admin.getPassword(),
                Collections.singletonList(authority)
        );
    }
    public void registerNewAdmin(String email, String password, String role) {
        if (adminRepository.findByEmail(email) != null) {
            throw new IllegalStateException("Email already exists");
        }
        Admin newAdmin = new Admin();
        newAdmin.setEmail(email);
        newAdmin.setPassword(passwordEncoder.encode(password));
        newAdmin.setRole(role);  // Set the role
        adminRepository.save(newAdmin);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.findByEmail(email) != null;
    }

}