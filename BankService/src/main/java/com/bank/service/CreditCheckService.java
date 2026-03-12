package com.bank.service;

import com.bank.model.CreditRequest;
import com.bank.model.CreditResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CreditCheckService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.response.routingkey}")
    private String responseRoutingKey;

    // Hardcoded fixed credit limit for all users as required by TP
    private static final double INITIAL_CREDIT_LIMIT = 10000.0;

    // Simulate a database of user balances
    private final java.util.Map<String, Double> userBalances = new java.util.concurrent.ConcurrentHashMap<>();

    // Optional TP Extension: Sensible users for fraud detection
    private final List<String> SENSIBLE_USERS = Arrays.asList("fraudster1", "hacker99", "sensible_user");

    public CreditCheckService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        // Initialize the default user from CarServiceApp
        userBalances.put("quentin", INITIAL_CREDIT_LIMIT);
    }

    public double getBalance(String userId) {
        return userBalances.getOrDefault(userId, INITIAL_CREDIT_LIMIT);
    }

    public java.util.Map<String, Double> getAllBalances() {
        return userBalances;
    }

    @RabbitListener(queues = "${app.rabbitmq.request.queue}")
    public void processCreditRequest(CreditRequest request) {
        System.out.println("Received credit request: " + request);

        CreditResponse response = new CreditResponse();
        response.setUserId(request.getUserId());

        String userId = request.getUserId();
        double currentBalance = getBalance(userId);

        // Fraud detection logic
        if (SENSIBLE_USERS.contains(userId)) {
            System.out.println("WARNING: Sensible user detected (" + userId + "). Manual validation required.");
            try {
                Thread.sleep(3000); // Simulate manual operator review delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            response.setApproved(false);
            response.setMessage("Manual Validation: Rejected due to Fraud Detection System.");
        } else {
            // Auto Validate Credit logic
            if (request.getAmount() <= currentBalance) {
                // Deduct from balance
                double newBalance = currentBalance - request.getAmount();
                userBalances.put(userId, newBalance);

                response.setApproved(true);
                response.setMessage(String.format("Auto Validate: Approved. New balance: %.2f €", newBalance));
                System.out.println("Deducted " + request.getAmount() + " from " + userId + ". New balance: "
                        + newBalance);
            } else {
                response.setApproved(false);
                response.setMessage(
                        String.format("Auto Validate: Refused. Insufficient funds. Balance: %.2f €", currentBalance));
            }
        }

        System.out.println("Sending response: " + response);
        rabbitTemplate.convertAndSend(exchange, responseRoutingKey, response);
    }
}
