package com.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour afficher l'interface web de la banque.
 */
@Controller
public class BankDashboardController {

    private final com.bank.service.CreditCheckService creditService;

    public BankDashboardController(com.bank.service.CreditCheckService creditService) {
        this.creditService = creditService;
    }

    // Affiche la page d'accueil avec les soldes de tous les utilisateurs
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("balances", creditService.getAllBalances());
        return "dashboard";
    }

    // Affiche la page de connexion à la banque
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
