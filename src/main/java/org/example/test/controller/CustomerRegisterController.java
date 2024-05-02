package org.example.test.controller;


import org.example.test.model.Customer;
import org.example.test.service.CustomerServiceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class CustomerRegisterController {

    private final CustomerServiceRegister customerService;  // Corrected type

    @Autowired
    public CustomerRegisterController(CustomerServiceRegister customerService) {  // Corrected constructor name
        this.customerService = customerService;
    }

    @ModelAttribute("customer")
    public Customer customer() {
        return new Customer();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "home/register";
    }

    @PostMapping
    public String registerCustomerAccount(@ModelAttribute("customer") Customer customer, RedirectAttributes redirectAttributes) {
        try {
            customerService.save(customer);  // Corrected service call
            return "redirect:/register?success";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register?error";
        }
    }
}
