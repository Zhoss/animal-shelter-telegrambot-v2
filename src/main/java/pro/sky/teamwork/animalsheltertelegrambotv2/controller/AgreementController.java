package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;

import java.util.List;

@RestController
@RequestMapping("/agreement")
public class AgreementController {

    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @Operation(
            summary = "Добавление договора о принятии",
            tags = "Договор о принятии"
    )
    @PostMapping
    public ResponseEntity<AgreementRecord> addAgreement(
            @RequestBody AgreementRecord agreementRecord) {
        return ResponseEntity.ok(agreementService.addAgreement(agreementRecord));
    }

    @Operation(
            summary = "Получение договора о принятии по ID договора",
            tags = "Договор о принятии"
    )
    @GetMapping("/{id}")
    public ResponseEntity<AgreementRecord> findAgreementById(
            @PathVariable Long id) {
        return ResponseEntity.ok(agreementService.findAgreementById(id));
    }

    @Operation(
            summary = "Изменение (полное) договора о принятии",
            tags = "Договор о принятии"
    )
    @PutMapping
    public ResponseEntity<AgreementRecord> editAgreement(
            @RequestBody AgreementRecord agreementRecord) {
        return ResponseEntity.ok(agreementService.editAgreement(agreementRecord));
    }

    @Operation(
            summary = "Удаление договора о принятии по ID договора",
            tags = "Договор о принятии"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgreementById(
            @PathVariable long id) {
        agreementService.deleteAgreement(id);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Получение всех договоров о принятии",
            tags = "Договор о принятии"
    )
    @GetMapping
    public ResponseEntity<List<AgreementRecord>> findAllAgreements() {
        return ResponseEntity.ok(this.agreementService.findAllAgreements());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
