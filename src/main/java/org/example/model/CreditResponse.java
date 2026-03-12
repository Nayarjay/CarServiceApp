package org.example.model;

import java.io.Serializable;

/**
 * Modèle de données représentant la réponse renvoyée par la banque.
 */
public class CreditResponse implements Serializable {

    private String userId; // Pour qui est la réponse ?
    private boolean approved; // Est-ce que c'est accepté (true) ou refusé (false) ?
    private String message; // Message explicatif (ex: "Solde insuffisant")

    public CreditResponse() {
    }

    public CreditResponse(String userId, boolean approved, String message) {
        this.userId = userId;
        this.approved = approved;
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
