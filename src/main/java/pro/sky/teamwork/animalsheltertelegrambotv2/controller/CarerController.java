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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;

import java.util.List;

@RestController
@RequestMapping("/carer")
public class CarerController {
    private final CarerService carerService;

    public CarerController(CarerService carerService) {
        this.carerService = carerService;
    }

    @Operation(
            summary = "Поиск опекуна по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carer added",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogCarer[].class)))),
                    @ApiResponse(responseCode = "400", description = "Incorrect id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogCarer[].class)))),
                    @ApiResponse(responseCode = "404", description = "Carer with current id not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogCarer[].class))))
            },
            tags = "Опекун"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CarerRecord> findCarer(
            @Parameter(description = "ID Опекуна")
            @PathVariable long id,
            @RequestParam PetType petType) {
        return ResponseEntity.ok(this.carerService.findCarer(id, petType));
    }

    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Редактируемый опекун",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = DogCarer.class),
                            examples = {
                                    @ExampleObject(
                                            value = "{\"id\": 0,"
                                                    + "\"petType\": \"CAT/DOG\","
                                                    + "\"secondName\": \"Иванов\","
                                                    + "\"firstName\": \"Иван\","
                                                    + "\"patronymic\": \"Иванович\","
                                                    + "\"age\": 30,"
                                                    + "\"phoneNumber\": \"+7(999)1234567\","
                                                    + "\"passportNumber\": \"1234 123456\","
                                                    + "\"petId\": 1"
                                                    + "}"
                                    )
                            }
                    )
            ),
            summary = "Редактирование данных опекуна",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carer added"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }, tags = "Опекун"
    )
    @PatchMapping
    public ResponseEntity<CarerRecord> editCarer(
            @RequestBody CarerRecord carerRecord) {
        return ResponseEntity.ok(this.carerService.editCarer(carerRecord));
    }

    @Operation(
            summary = "Удаление записи об опекуне",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carer information delete",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = DogCarer[].class)))),
            },
            tags = "Опекун"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCarer(
            @Parameter(description = "ID Опекуна")
            @PathVariable long id,
            @RequestParam PetType petType) {
        this.carerService.deleteCarer(id, petType);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Поиск опекуна по номеру телефона",
            tags = "Опекун"
    )
    @GetMapping("/phone-number")
    public ResponseEntity<CarerRecord> findCarerByPhoneNumber(
            @Parameter(description = "Номер телефона опекуна", example = "+7(123)1234567")
            @RequestParam String phone,
            @RequestParam PetType petType) {
        return ResponseEntity.ok(this.carerService.findCarerByPhoneNumber(phone, petType));
    }

    @Operation(
            summary = "Поиск всех опекунов",
            tags = "Опекун"
    )
    @GetMapping
    public ResponseEntity<List<CarerRecord>> findAllCarers(@RequestParam PetType petType) {
        return ResponseEntity.ok(this.carerService.findAllCarers(petType));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
