package org.example.Controller;

import org.example.model.*;
import org.example.repository.CarServiceRepository;
import org.example.repository.OperationRepository;
import org.example.service.CreditRequestPublisher;
import org.example.service.CreditResponseListener;
import org.example.service.RabbitLogService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RestController
public class RentalController {

    private final CarServiceRepository carRepository;
    private final OperationRepository operationRepository;
    private final CreditRequestPublisher creditRequestPublisher;
    private final CreditResponseListener creditResponseListener;
    private final RabbitLogService logService;

    public RentalController(CarServiceRepository carRepository,
                            OperationRepository operationRepository,
                            CreditRequestPublisher creditRequestPublisher,
                            CreditResponseListener creditResponseListener,
                            RabbitLogService logService) {
        this.carRepository = carRepository;
        this.operationRepository = operationRepository;
        this.creditRequestPublisher = creditRequestPublisher;
        this.creditResponseListener = creditResponseListener;
        this.logService = logService;
    }

    @GetMapping("/vehicles")
    public List<CarService> listVehicles() {
        return carRepository.findAll();
    }

    @PostMapping("/rental/vehicle/{id}")
    public Map<String, Object> rentVehicle(@PathVariable Long id,
                                           @RequestParam(required = false) String userId,
                                           Principal principal) {
        String user = (userId != null) ? userId : (principal != null ? principal.getName() : "anonymous");
        return processOperation(id, user, OperationType.RENT);
    }

    @PostMapping("/purchase/vehicle/{id}")
    public Map<String, Object> purchaseVehicle(@PathVariable Long id,
                                               @RequestParam(required = false) String userId,
                                               Principal principal) {
        String user = (userId != null) ? userId : (principal != null ? principal.getName() : "anonymous");
        return processOperation(id, user, OperationType.BUY);
    }

    @GetMapping("/operations")
    public List<Operation> listOperations() {
        return operationRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/api/logs")
    public Map<String, Object> getLogs(@RequestParam(defaultValue = "0") int since) {
        return Map.of(
                "logs", logService.getLogsSince(since),
                "total", logService.getLogCount()
        );
    }

    private Map<String, Object> processOperation(Long vehicleId, String userId, OperationType type) {
        CarService vehicle = carRepository.findById(vehicleId).orElse(null);
        if (vehicle == null) {
            return Map.of("status", "ERROR", "message", "Vehicle not found with id: " + vehicleId);
        }

        double amount = (type == OperationType.RENT) ? vehicle.getRentalPricePerDay() : vehicle.getCarPrice();

        CreditRequest request = new CreditRequest(userId, amount, type.name());

        CountDownLatch latch = creditResponseListener.createLatch(userId);
        creditRequestPublisher.sendCreditRequest(request);

        try {
            boolean received = latch.await(10, TimeUnit.SECONDS);
            if (!received) {
                Operation op = new Operation(userId, vehicle, type, false, "Timeout: Bank service did not respond");
                operationRepository.save(op);
                return Map.of(
                        "status", "ERROR",
                        "operation", type.name(),
                        "message", "Timeout: Bank service did not respond",
                        "amount", amount,
                        "vehicle", vehicle.getBrand() + " " + vehicle.getModel()
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Map.of("status", "ERROR", "message", "Interrupted while waiting for bank response");
        }

        CreditResponse response = creditResponseListener.getResponse(userId);
        if (response == null) {
            return Map.of("status", "ERROR", "message", "No response received from bank");
        }

        Operation operation = new Operation(userId, vehicle, type, response.isApproved(), response.getMessage());
        operationRepository.save(operation);

        return Map.of(
                "status", response.isApproved() ? "APPROVED" : "REJECTED",
                "operation", type.name(),
                "message", response.getMessage(),
                "amount", amount,
                "vehicle", vehicle.getBrand() + " " + vehicle.getModel()
        );
    }
}
