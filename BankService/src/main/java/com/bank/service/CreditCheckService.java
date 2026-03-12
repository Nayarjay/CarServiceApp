package com.bank.service;

import com.bank.model.CreditRequest;
import com.bank.model.CreditResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service central de la banque.
 * Il écoute les demandes de crédit et décide d'approuver ou non l'achat.
 */
@Service
public class CreditCheckService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.response.routingkey}")
    private String responseRoutingKey;

    // Le solde de départ pour les nouveaux utilisateurs
    private static final double INITIAL_CREDIT_LIMIT = 10000.0;

    // Une mini base de données en mémoire pour stocker l'argent des utilisateurs
    private final java.util.Map<String, Double> userBalances = new java.util.concurrent.ConcurrentHashMap<>();

    // Liste des utilisateurs suspects pour la simulation de fraude
    private final List<String> SENSIBLE_USERS = Arrays.asList("fraudster1", "hacker99", "sensible_user");

    public CreditCheckService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // On crée un compte avec 10 000€ pour l'utilisateur par défaut "quentin"
        userBalances.put("quentin", INITIAL_CREDIT_LIMIT);
    }

    // Récupère l'argent restant d'un utilisateur
    public double getBalance(String userId) {
        return userBalances.getOrDefault(userId, INITIAL_CREDIT_LIMIT);
    }

    // Récupère la liste de tous les comptes pour l'affichage
    public java.util.Map<String, Double> getAllBalances() {
        return userBalances;
    }

    // CETTE FONCTION S'ACTIVE AUTOMATIQUEMENT QUAND UN MESSAGE ARRIVE DANS RABBITMQ
    @RabbitListener(queues = "${app.rabbitmq.request.queue}")
    public void processCreditRequest(CreditRequest request) {
        System.out.println("Demande reçue : " + request);

        CreditResponse response = new CreditResponse();
        response.setUserId(request.getUserId());

        String userId = request.getUserId();
        double currentBalance = getBalance(userId);

        // ETAPE 1 : Vérifier si l'utilisateur est un fraudeur
        if (SENSIBLE_USERS.contains(userId)) {
            System.out.println("ALERTE FRAUDE : " + userId);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            response.setApproved(false);
            response.setMessage("Refusé : Détection de fraude, examen manuel requis.");
        } else {
            // ETAPE 2 : Vérifier si l'utilisateur a assez d'argent
            if (request.getAmount() <= currentBalance) {
                // On soustrait le prix de l'achat
                double newBalance = currentBalance - request.getAmount();
                userBalances.put(userId, newBalance);

                response.setApproved(true);
                response.setMessage(String.format("Approuvé ! Nouveau solde : %.2f €", newBalance));
                System.out.println("Deducted " + request.getAmount() + " from " + userId + ". New balance: "
                        + newBalance); // This line was not in the instruction, keeping it as is.
            } else {
                response.setApproved(false);
                response.setMessage(String.format("Refusé : Fonds insuffisants (Solde : %.2f €)", currentBalance));
            }
        }

        // ETAPE 3 : Envoyer la réponse à l'application de location via RabbitMQ
        System.out.println("Réponse envoyée : " + response);
        rabbitTemplate.convertAndSend(exchange, responseRoutingKey, response);
    }
}
