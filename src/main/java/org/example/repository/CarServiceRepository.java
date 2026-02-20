package org.example.repository;


import org.example.model.CarService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarServiceRepository extends JpaRepository<CarService, Long> {
}
