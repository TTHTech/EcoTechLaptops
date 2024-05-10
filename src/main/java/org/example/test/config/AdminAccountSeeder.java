package org.example.test.config;

import org.example.test.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountSeeder implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Override
    public void run(String... args) throws Exception {
        // Đảm bảo rằng bạn chỉ tạo tài khoản admin nếu nó chưa tồn tại để tránh tạo trùng lặp
        if (!adminService.existsByEmail("admin@example.com")) {
            adminService.registerNewAdmin("admin@example.com", "admin123", "ROLE_ADMIN");

        }
    }
}
