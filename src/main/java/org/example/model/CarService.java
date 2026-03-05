package org.example.model;

import jakarta.persistence.*;

@Entity
public class CarService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @jakarta.validation.constraints.NotNull
    private CarType type;

    @jakarta.validation.constraints.NotNull
    private Double rentalPricePerDay;

    @jakarta.validation.constraints.NotNull
    private Double carPrice;

    @jakarta.validation.constraints.NotBlank
    private String brand;

    @jakarta.validation.constraints.NotBlank
    private String model;

    @jakarta.validation.constraints.NotNull
    private java.time.LocalDate purchaseDate;

    @jakarta.validation.constraints.NotNull
    private Integer maxPassengers;

    @jakarta.validation.constraints.NotNull
    private Integer maxSpeed;

    private boolean airConditioner;
    private boolean automaticTransmission;

    public CarService() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarType getType() {
        return type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    public Double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(Double rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public Double getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(Double carPrice) {
        this.carPrice = carPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public java.time.LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(java.time.LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getMaxPassengers() {
        return maxPassengers;
    }

    public void setMaxPassengers(Integer maxPassengers) {
        this.maxPassengers = maxPassengers;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(boolean airConditioner) {
        this.airConditioner = airConditioner;
    }

    public boolean isAutomaticTransmission() {
        return automaticTransmission;
    }

    public void setAutomaticTransmission(boolean automaticTransmission) {
        this.automaticTransmission = automaticTransmission;
    }
}
