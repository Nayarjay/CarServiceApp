package org.example.Controller;

import org.example.repository.CarServiceRepository;
import org.example.repository.OperationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RentalViewController {

    private final CarServiceRepository carRepository;
    private final OperationRepository operationRepository;

    public RentalViewController(CarServiceRepository carRepository, OperationRepository operationRepository) {
        this.carRepository = carRepository;
        this.operationRepository = operationRepository;
    }

    @GetMapping("/rent")
    public String rentPage(Model model) {
        model.addAttribute("vehicles", carRepository.findAll());
        return "rent";
    }

    @GetMapping("/history")
    public String historyPage(Model model) {
        model.addAttribute("operations", operationRepository.findAllByOrderByCreatedAtDesc());
        return "history";
    }
}
