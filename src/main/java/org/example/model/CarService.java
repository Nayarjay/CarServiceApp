package org.example.model;
package com.example.model.carservice.entity;

import jakarta.persistence.*;

@Entity
public class CarService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String carModel;
    private String serviceType;
    private Double price;

    public CarService() {}

    public CarService(String customerName, String carModel, String serviceType, Double price) {
        this.customerName = customerName;
        this.carModel = carModel;
        this.serviceType = serviceType;
        this.price = price;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
