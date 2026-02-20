package org.example.Controller;


import org.example.model.CarService;
import org.example.repository.CarServiceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarServiceController {

    private final CarServiceRepository repository;

    public CarServiceController(CarServiceRepository repository) {
        this.repository = repository;
    }

    // Home page
    @GetMapping("/")
    public String home() {
        return "home";
    }

    // Show all services
    @GetMapping("/services")
    public String listServices(Model model) {
        model.addAttribute("services", repository.findAll());
        return "list-services";
    }

    // Show form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("carService", new CarService());
        return "add-service";
    }

    // Save service
    @PostMapping("/save")
    public String saveService(@ModelAttribute CarService carService) {
        repository.save(carService);
        return "redirect:/services";
    }

    // Edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CarService service = repository.findById(id).orElseThrow();
        model.addAttribute("carService", service);
        return "edit-service";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/services";
    }
}