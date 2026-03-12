package com.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BankDashboardController {

    private final com.bank.service.CreditCheckService creditService;

    public BankDashboardController(com.bank.service.CreditCheckService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("balances", creditService.getAllBalances());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
