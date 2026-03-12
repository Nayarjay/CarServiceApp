package org.example.model;

import java.io.Serializable;

/**
 * Modèle de données représentant une demande de crédit envoyée par la location.
 */
public class CreditRequest implements Serializable {

    private String userId; // qui achète ?
    private Double amount; // Combien ça coûte ?
    private String operationType; // Achat ou Location ?

    public CreditRequest() {
    }

    public CreditRequest(String userId, Double amount, String operationType) {
        this.userId = userId;
        this.amount = amount;
        this.operationType = operationType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
