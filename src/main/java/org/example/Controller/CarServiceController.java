package org.example.Controller;

import org.example.model.CarService;
import org.example.model.CarType;
import org.example.repository.CarServiceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/")
public class CarServiceController {

    private final CarServiceRepository repository;

    public CarServiceController(CarServiceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/cars")
    public String listCars(Model model) {
        model.addAttribute("cars", repository.findAll());
        return "list-cars";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("car", new CarService());
        model.addAttribute("carTypes", CarType.values());
        return "create-car";
    }

    @PostMapping("/save")
    public String saveCar(@Valid @ModelAttribute("car") CarService car, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("carTypes", CarType.values());
            return "create-car";
        }
        repository.save(car);
        return "redirect:/cars";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/cars";
    }
}