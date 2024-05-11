package org.example.test.controller;


import jakarta.servlet.http.HttpSession;
import org.example.test.model.Customer;
import org.example.test.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChangePasswordController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<?> changeUserPassword(HttpSession session,
                                                @RequestParam("currentPassword") String currentPassword,
                                                @RequestParam("newPassword") String newPassword) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return new ResponseEntity<>("Not logged in", HttpStatus.UNAUTHORIZED);
        }

        boolean updateSuccess = customerService.changePassword(customer.getId(), currentPassword, newPassword);
        if (!updateSuccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Current password is incorrect or failed to update.");
        }
        return ResponseEntity.ok("Password changed successfully.");
    }

}
