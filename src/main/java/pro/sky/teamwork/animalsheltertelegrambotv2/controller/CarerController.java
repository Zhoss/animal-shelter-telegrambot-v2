package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;

@RestController
@RequestMapping("/carer")
public class CarerController {
    private final CarerService carerService;

    public CarerController(CarerService carerService) {
        this.carerService = carerService;
    }

    @PostMapping
    public ResponseEntity<Carer> addCarer(@RequestParam String fullName,
                                          @RequestParam int age,
                                          @RequestParam String phoneNumber) {
        return ResponseEntity.ok(this.carerService.addCarer(fullName, age, phoneNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carer> findCarer(@PathVariable long id) {
        return ResponseEntity.ok(this.carerService.findCarer(id));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
