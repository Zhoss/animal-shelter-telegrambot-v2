package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DogService;

@RestController
@RequestMapping("/dog")
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @PostMapping
    public ResponseEntity<DogRecord> addDog(@RequestBody DogRecord dogRecord) {
        return ResponseEntity.ok(this.dogService.addDog(dogRecord));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogRecord> findDog(@PathVariable long id) {
        return ResponseEntity.ok(this.dogService.findDog(id));
    }

    @PutMapping
    public ResponseEntity<DogRecord> editDog(@RequestBody DogRecord dogRecord) {
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
