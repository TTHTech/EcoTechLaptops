package org.example.test.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "home/login";
    }

    @GetMapping("/post-login")
    public String handlePostLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().toString();

        if (role.contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        } else {
            return "redirect:/index";
        }
    }
}
