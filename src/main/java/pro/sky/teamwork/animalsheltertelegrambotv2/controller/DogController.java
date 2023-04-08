package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DogService;

@RestController
@RequestMapping("/dog")
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping
    public ResponseEntity<Dog> addDog(@RequestBody DogRecord dogRecord) {
        return ResponseEntity.ok(this.dogService.addDog(dogRecord));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dog> findDog(@PathVariable long id) {
        return ResponseEntity.ok(this.dogService.findDog(id));
    }

    @PutMapping
    public ResponseEntity<Dog> editDog(@RequestBody DogRecord dogRecord) {
        return ResponseEntity.ok(this.dogService.editDog(dogRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDog(@PathVariable long id) {
        this.dogService.deleteDog(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
