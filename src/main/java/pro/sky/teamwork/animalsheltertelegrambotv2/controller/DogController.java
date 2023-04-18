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
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord2;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord3;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DogService;

import java.util.List;

/**
 * Класс описывающий работу контроллера собаки
 */
@RestController
@RequestMapping("/dog")
public class DogController {
    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Добавление собаки",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Dog.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"id\": 0,"
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
            summary = "Добавление данных о собаке",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dog added"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }, tags = "Собака"
    )
    @PostMapping
    public ResponseEntity<DogRecord> addDog(@RequestBody DogRecord dogRecord) {
        return ResponseEntity.ok(this.dogService.addDog(dogRecord));
    }

    @Operation(
            summary = "Поиск информации о собаке по ID собаки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dog added",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class)))),
                    @ApiResponse(responseCode = "400", description = "Incorrect id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class)))),
                    @ApiResponse(responseCode = "404", description = "Dog with current id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class))))
            },
            tags = "Собака"
    )
    @GetMapping("/{id}")
    public ResponseEntity<DogRecord> findDog(@Parameter(description = "Введите ID Собаки") @PathVariable long id) {
        return ResponseEntity.ok(this.dogService.findDog(id));
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Обновление данных о собаке",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Dog.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"id\": 0,"
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
            summary = "Обновление данных о собаке",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dog information update"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }, tags = "Собака"
    )
    @PutMapping
    public ResponseEntity<DogRecord> editDog(@RequestBody DogRecord dogRecord) {
        return ResponseEntity.ok(this.dogService.editDog(dogRecord));
    }

    @Operation(
            summary = "Удаление записи о собаке по ID собаки",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dog information delete",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Dog[].class)))),
            },
            tags = "Собака"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDog(@Parameter(description = " Введите ID Собаки для удаления")
                                       @PathVariable long id) {
        this.dogService.deleteDog(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение данных о всех собаках",
            tags = "Собака"
    )
    @GetMapping
    public ResponseEntity<List<DogRecord>> findAllDogs() {
        return ResponseEntity.ok(this.dogService.findAllDogs());
    }

    @Operation(
            summary = "Новый - Изменение состояния поля \" На испытательном сроке\"",
            tags = "Собака"
    )
    @PatchMapping("/")
    public ResponseEntity<Dog> editDog(
            @RequestBody DogRecord2 dogRecord2) {
        return ResponseEntity.ok(dogService.editDog2(dogRecord2));
    }

    @Operation(
            summary = "Новый - Изменение состояния поля \" Забран из приюта\"",
            tags = "Собака")

    @PatchMapping("")
    public ResponseEntity<Dog> editDog(
            @RequestBody DogRecord3 dogRecord3) {
        return ResponseEntity.ok(dogService.editDog3(dogRecord3));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
