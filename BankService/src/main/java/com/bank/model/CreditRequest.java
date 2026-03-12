package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modèle de données représentant une demande de crédit envoyée par la location.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {
    private String userId; // qui achète ?
    private double amount; // Combien ça coûte ?
    private String operationType; // Achatx²

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return  amount;
    }
}
