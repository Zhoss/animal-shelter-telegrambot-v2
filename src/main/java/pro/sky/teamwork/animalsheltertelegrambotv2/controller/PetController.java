package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.PetService;

import java.util.List;

/**
 * Класс описывающий работу контроллера собаки
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление питомца",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Dog.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"id\": 0,"
                                                    + "\"petType\": \"тип животного\","
                                                    + "\"name\": \"Кличка\","
                                                    + "\"breed\": \"Порода\","
                                                    + "\"coatColor\": \"Цвет\","
                                                    + "\"age\": 3,"
                                                    + "\"features\": \"особенности\""
                                                    + "}"
                                    )
                            }
                    )
            ),
            summary = "Добавление данных о питомце",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pet added"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }, tags = "Питомец"
    )
    @PostMapping
    public ResponseEntity<PetRecord> addDog(@RequestBody PetRecord petRecord) {
        return ResponseEntity.ok(this.petService.addPet(petRecord));
    }

    @Operation(
            summary = "Поиск информации о питомце по ID питомца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pet added",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class)))),
                    @ApiResponse(responseCode = "400", description = "Incorrect id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class)))),
                    @ApiResponse(responseCode = "404", description = "Dog with current id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class))))
            },
            tags = "Питомец"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PetRecord> findDog(@Parameter(description = "Введите ID питомца") @PathVariable long id,
                                             @RequestParam String petType) {
        return ResponseEntity.ok(this.petService.findPet(id, petType));
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновление данных о питомце",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Dog.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"id\": 0,"
                                                    + "\"petType\": \"тип животного\","
                                                    + "\"name\": \"Кличка\","
                                                    + "\"breed\": \"Порода\","
                                                    + "\"coatColor\": \"Цвет\","
                                                    + "\"age\": 3,"
                                                    + "\"features\": \"особенности\""
                                                    + "}"
                                    )
                            }
                    )
            ),
            summary = "Обновление данных о питомце",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pet information update"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }, tags = "Питомец"
    )
    @PutMapping
    public ResponseEntity<PetRecord> editDog(@RequestBody PetRecord petRecord) {
        return ResponseEntity.ok(this.petService.editDog(petRecord));
    }

    @Operation(
            summary = "Удаление записи о питомце по ID питомца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pet information delete",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class)))),
            },
            tags = "Питомец"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDog(@Parameter(description = " Введите ID питомца для удаления")
                                       @PathVariable long id,
                                       @RequestParam String petType) {
        this.petService.deletePet(id, petType);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение данных о всех питомцах (по типу)",
            tags = "Питомец"
    )
    @GetMapping
    public ResponseEntity<List<PetRecord>> findAllPets(@RequestParam String petType) {
        return ResponseEntity.ok(this.petService.findAllPets(petType));
    }

    @Operation(
            summary = "Изменение состояния \"На испытательном сроке\"",
            tags = "Питомец"
    )
    @PatchMapping("/is-taken/{id}")
    public ResponseEntity<?> changeIsTakenStatus(@PathVariable long id,
                                                 @RequestParam String petType,
                                                 @RequestParam boolean isTaken) {
        this.petService.changeIsTakenStatus(id, petType, isTaken);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Изменение состояния \"Взят из приюта\"",
            tags = "Питомец")
    @PatchMapping("/on-probation/{id}")
    public ResponseEntity<?> changeOnProbationStatus(@PathVariable long id,
                                                     @RequestParam String petType,
                                                     @RequestParam boolean onProbation) {
        this.petService.changeOnProbationStatus(id, petType, onProbation);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
