package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;

@RestController
@RequestMapping("/agreement")
public class AgreementController {

    private AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    @PostMapping("/")
    public ResponseEntity<Agreement> addAgreement(@RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.createAgreement(agreement));
    }
}
