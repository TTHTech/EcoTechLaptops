package org.example.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminStatisticController {

    @GetMapping("/admin/statistics")
    public String adminStatistics(Model model) {
        return "admin/statistics";
    }
}
