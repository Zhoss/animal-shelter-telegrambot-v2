package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.AgreementRepository;

import java.time.LocalDate;

@Service
public class AgreementService {

private final AgreementRepository agreementRepository;

    public AgreementService(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    /**
     * Метод создания договора.
     * @param agreement модель класса Agreement
     * @return создан договор
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.AgreementRepository
     */
    public Agreement createAgreement(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

}
