package org.example.config;

import org.example.model.AppUser;
import org.example.model.CarService;
import org.example.model.CarType;
import org.example.repository.AppUserRepository;
import org.example.repository.CarServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final CarServiceRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository userRepository, CarServiceRepository carRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("quentin")) {
            userRepository.save(new AppUser("quentin", passwordEncoder.encode("quentin")));
        }

        if (carRepository.count() == 0) {
            CarService clio = new CarService();
            clio.setBrand("Renault");
            clio.setModel("Clio");
            clio.setType(CarType.SEDAN);
            clio.setRentalPricePerDay(500.0);
            clio.setCarPrice(15000.0);
            clio.setPurchaseDate(LocalDate.of(2024, 1, 15));
            clio.setMaxPassengers(5);
            clio.setMaxSpeed(180);
            clio.setAirConditioner(true);
            clio.setAutomaticTransmission(false);
            carRepository.save(clio);

            CarService bmw = new CarService();
            bmw.setBrand("BMW");
            bmw.setModel("Serie 3");
            bmw.setType(CarType.SEDAN);
            bmw.setRentalPricePerDay(800.0);
            bmw.setCarPrice(45000.0);
            bmw.setPurchaseDate(LocalDate.of(2023, 6, 20));
            bmw.setMaxPassengers(5);
            bmw.setMaxSpeed(250);
            bmw.setAirConditioner(true);
            bmw.setAutomaticTransmission(true);
            carRepository.save(bmw);

            CarService mercedes = new CarService();
            mercedes.setBrand("Mercedes");
            mercedes.setModel("Classe S");
            mercedes.setType(CarType.SEDAN);
            mercedes.setRentalPricePerDay(1200.0);
            mercedes.setCarPrice(120000.0);
            mercedes.setPurchaseDate(LocalDate.of(2024, 3, 10));
            mercedes.setMaxPassengers(5);
            mercedes.setMaxSpeed(250);
            mercedes.setAirConditioner(true);
            mercedes.setAutomaticTransmission(true);
            carRepository.save(mercedes);

            System.out.println("[INIT] 3 test vehicles created: Renault Clio, BMW Serie 3, Mercedes Classe S");
        }
    }
}
