package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;

@RestController
@RequestMapping("/agreement")
public class AgreementController {

    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/")
    public ResponseEntity<AgreementRecord> addAgreement(
            @RequestBody AgreementRecord agreementRecord) {
        return ResponseEntity.ok(agreementService.addAgreement(agreementRecord));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgreementRecord> findAgreementById(
            @PathVariable Long id) {
        return ResponseEntity.ok(agreementService.findAgreementById(id));
    }

    @PutMapping
    public ResponseEntity<AgreementRecord> editAgreement(
            @RequestBody AgreementRecord agreementRecord) {
        return ResponseEntity.ok(agreementService.editAgreement(agreementRecord));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Agreement> deleteAgreementById(
            @PathVariable long id) {
        agreementService.deleteAgreement(id);
        return ResponseEntity.ok().build();
    }
}
