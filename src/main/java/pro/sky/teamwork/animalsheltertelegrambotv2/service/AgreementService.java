package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.AgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AgreementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportService.class);
    private final AgreementRepository agreementRepository;
    private final ModelMapper modelMapper;
    private final DogRepository dogRepository;

    public AgreementService(AgreementRepository agreementRepository,
                            ModelMapper modelMapper, DogRepository dogRepository) {
        this.agreementRepository = agreementRepository;
        this.modelMapper = modelMapper;
        this.dogRepository = dogRepository;
    }

    @Transactional
    public AgreementRecord addAgreement(AgreementRecord agreementRecord) {
        Agreement agreement = agreementRepository
                .save(modelMapper.mapToAgreementEntity(agreementRecord));
        Dog dog = agreement.getCarer().getDog();
        dog.setTaken(true);
        dog.setOnProbation(true);
        this.dogRepository.save(dog);
        return modelMapper.mapToAgreementRecord(agreement);
    }

    /**
     * Метод создания договора.
     * @param agreement модель класса Agreement
     * @return создан договор
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.AgreementRepository
     */
    public Agreement createAgreement(Agreement agreement) {
        return agreementRepository.save(agreement);
    @Transactional
    public AgreementRecord findAgreementById(long id) {
        Agreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Договор не найден"));
        return modelMapper.mapToAgreementRecord(agreement);
    }

    @Transactional
    public AgreementRecord editAgreement(AgreementRecord agreementRecord) {
        Agreement agreement = agreementRepository
                .save(modelMapper.mapToAgreementEntity(agreementRecord));
        return modelMapper.mapToAgreementRecord(agreement);
    }

    @Transactional
    public void deleteAgreement(long id) {
        agreementRepository.deleteById(id);
    }

    @Transactional
    public List<AgreementRecord> findAllAgreements() {
        return this.agreementRepository.findAll().stream()
                .map(this.modelMapper::mapToAgreementRecord)
                .collect(Collectors.toList());
    }

    public List<Agreement> findAllAgreementsByDate(LocalDate localDate) {
        return this.agreementRepository.findAll().stream()
                .filter(e -> Objects.equals(e.getConclusionDate(), localDate))
                .collect(Collectors.toList());
    }
}
