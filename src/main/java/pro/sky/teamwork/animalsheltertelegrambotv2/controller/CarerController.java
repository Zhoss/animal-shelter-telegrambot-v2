package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
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

    @Operation(
            summary = "Добавление данных опекуна",
            responses = {
                            @ApiResponse(responseCode = "200", description = "OK"),
                            @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                            @ApiResponse(responseCode = "404", description = "Customer not found"),
                            @ApiResponse(responseCode = "500", description = "Internal server error")
                    }, tags = "Опекун"
                    )

                    @PostMapping
            public ResponseEntity<Carer>addCarer(@Parameter(required = true, allowEmptyValue = true, name = "ФИО Опекуна") @RequestParam String fullName,
            @Parameter(required = true, allowEmptyValue = true, name = "Возраст опекуна")@RequestParam int age,
            @Parameter(required = true, allowEmptyValue = true, example = "+7 999 5553399", name = "Номер телефона опекуна")@RequestParam String phoneNumber) {
        return ResponseEntity.ok(this.carerService.addCarer(fullName, age, phoneNumber));
    }

    @Operation(
            summary = "Поиск опекуна по ID",
            responses = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Carer[].class)))}),
    }, tags = "Поиск опекуна по ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Carer> findCarer(@Parameter(description = "ID Опекуна",
            allowEmptyValue = true)
                                           @PathVariable long id) {
        return ResponseEntity.ok(this.carerService.findCarer(id));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
