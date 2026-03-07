package org.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private CarService vehicle;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private boolean approved;
    private String message;
    private LocalDateTime createdAt;

    public Operation() {
    }

    public Operation(String userId, CarService vehicle, OperationType operationType, boolean approved, String message) {
        this.userId = userId;
        this.vehicle = vehicle;
        this.operationType = operationType;
        this.approved = approved;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public CarService getVehicle() { return vehicle; }
    public void setVehicle(CarService vehicle) { this.vehicle = vehicle; }
    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
