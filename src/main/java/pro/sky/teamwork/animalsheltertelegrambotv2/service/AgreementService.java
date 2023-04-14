package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.AgreementRepository;

import java.time.LocalDate;

@Service
public class AgreementService {

private final AgreementRepository agreementRepository;

    private final ModelMapper modelMapper;

    public AgreementService(AgreementRepository agreementRepository,
                            ModelMapper modelMapper) {
        this.agreementRepository = agreementRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AgreementRecord addAgreement(AgreementRecord agreementRecord) {
        var agreement = agreementRepository.save(
                modelMapper.mapToAgreementEntity(agreementRecord));
        return modelMapper.mapToAgreementRecord(agreement);
    }

    @Transactional
    public AgreementRecord findAgreementById(long id) {
        Agreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Договор не найден."));
        return modelMapper.mapToAgreementRecord(agreement);
    }

    @Transactional
    public AgreementRecord editAgreement(AgreementRecord agreementRecord) {
        Agreement agreement = agreementRepository.save(
                modelMapper.mapToAgreementEntity(agreementRecord));
        return modelMapper.mapToAgreementRecord(agreement);
    }

    @Transactional
    public void deleteAgreement(long id) {
        agreementRepository.deleteById(id);
    }

}
