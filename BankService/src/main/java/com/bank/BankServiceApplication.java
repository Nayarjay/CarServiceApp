package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale pour démarrer le micro-service de la Banque.
 */
@SpringBootApplication
public class BankServiceApplication {

    public static void main(String[] args) {
        // Lance l'application sur le port 8081
        SpringApplication.run(BankServiceApplication.class, args);
    }
}
