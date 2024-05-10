package org.example.test.controller;


import jakarta.servlet.http.HttpSession;
import org.example.test.model.Customer;
import org.example.test.service.CustomerService;
import org.example.test.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CustomerProfileController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UploadService uploadService;

    // Hiển thị trang profile
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer != null) {
            model.addAttribute("customer", customer);
            return "home/profile";
        } else {
            return "redirect:/login";
        }
    }

    // Xử lý upload ảnh và cập nhật thông tin
    @PostMapping("/profile")
    @ResponseBody
    public ResponseEntity<?> handleProfileUpdate(
            @RequestParam(value = "image", required = false) MultipartFile uploadfile,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            HttpSession session) {

        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return new ResponseEntity<>("No customer logged in", HttpStatus.FORBIDDEN);
        }

        if (uploadfile != null && !uploadfile.isEmpty()) {
            try {
                String filename = uploadService.handleSaveUploadFile(uploadfile, "avatar");
                customer.setImage(filename);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("Error during file upload", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Update customer information
        customer.setName(name);
        customer.setPhone(phone);
        customer.setAddress(address);
        customerService.updateCustomer(customer.getId(), customer);

        session.setAttribute("customer", customer);
        return ResponseEntity.ok("Profile updated successfully");
    }
}
