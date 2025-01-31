package pl.kamilracki.flota.controllers;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kamilracki.flota.enums.Month;
import pl.kamilracki.flota.models.entities.Car;
import pl.kamilracki.flota.models.entities.GetName;
import pl.kamilracki.flota.models.entities.MonthUse;
import pl.kamilracki.flota.services.CarService;
import pl.kamilracki.flota.services.MonthUseService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/monthUses")
public class MonthUseController {
    private final MonthUseService monthUseService;
    private final CarService carService;

    @ModelAttribute("login")
    public String loginUser() { return GetName.getLoginName();}

    @ModelAttribute("months")
    public List<String> getAllMonths(){return Month.valueOf();}


    @ModelAttribute("cars")
    public List<Car> getAllUsers() {
        return carService.findAll();
    }

    @GetMapping("/add/{id}")
    public String addMonthUse(@PathVariable("id") Long id, Model model) {
        Car car = carService.findById(id);
        Hibernate.initialize(car.getUser());
        Hibernate.initialize(car.getCarDetails());
        model.addAttribute("monthUse", new MonthUse());
        model.addAttribute("car", car);
        return "monthUses/form";
    }

    @PostMapping("/add/{id}")
    public String saveMonthUse(@Valid MonthUse monthUse, BindingResult result) {
        if (result.hasErrors()) {
            return "monthUses/form";
        }
        int start = monthUse.getNumbersOfKilometersOnStartOfMonth();
        int end = monthUse.getNumbersOfKilometersOnEndOfMonth();

        monthUse.setNumbersOfKilometersTraveledInThisMonth(end-start);
        monthUse.setCar(monthUse.getCar());
        monthUseService.saveMonthUse(monthUse);
        return "redirect:/monthUses/all/{id}";
    }

    @RequestMapping("/all/{id}")
    public String getAll(@PathVariable Long id, Model model) {
        Car car = carService.findById(id);
        String plateLicense = car.getCarDetails().getLicensePlate();
        if (car.getUser().getFullName() != null) {
            String userName = car.getUser().getFullName();
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", "Nie ma właściciela");
        }
        model.addAttribute("monthUse", monthUseService.findAll());
        model.addAttribute("plateLicense", plateLicense);
        return "monthUses/all";
    }
}
