package org.example.model;

import java.io.Serializable;

public class CreditResponse implements Serializable {

    private String userId;
    private boolean approved;
    private String message;

    public CreditResponse() {
    }

    public CreditResponse(String userId, boolean approved, String message) {
        this.userId = userId;
        this.approved = approved;
        this.message = message;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
