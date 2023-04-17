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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AgreementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportService.class);
    private final AgreementRepository agreementRepository;
    private final ModelMapper modelMapper;
    private final DogRepository dogRepository;

    public AgreementService(AgreementRepository agreementRepository,
                            ModelMapper modelMapper,
                            DogRepository dogRepository) {
        this.agreementRepository = agreementRepository;
        this.modelMapper = modelMapper;
        this.dogRepository = dogRepository;
    }

    @Transactional
    public AgreementRecord addAgreement(AgreementRecord agreementRecord) {
        if (agreementRecord != null) {
            Agreement agreement = agreementRepository
                    .save(modelMapper
                            .mapToAgreementEntity(agreementRecord));
            Dog dog = agreement.getCarer().getDog();
            dog.setTaken(true);
            dog.setOnProbation(true);
            this.dogRepository.save(dog);
            LOGGER.info("Was invoked method for adding agreement");
            return modelMapper.mapToAgreementRecord(agreement);
        } else {
            LOGGER.error("Input object 'agreementRecord' is null");
            throw new IllegalArgumentException("Требуется добавить договор");
        }
    }

    @Transactional(readOnly = true)
    public AgreementRecord findAgreementById(long id) {
        if (id > 0) {
            Agreement agreement = agreementRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
            LOGGER.info("Was invoked method to find agreement by id");
            return modelMapper.mapToAgreementRecord(agreement);
        } else {
            LOGGER.error("Input id = " + id + " for getting agreement is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id договора");
        }
    }

    @Transactional
    public AgreementRecord editAgreement(AgreementRecord agreementRecord) {
        if (agreementRecord != null) {
            LOGGER.info("Was invoked method to edit agreement");
            Agreement agreement = agreementRepository
                    .save(modelMapper.mapToAgreementEntity(agreementRecord));
            return modelMapper.mapToAgreementRecord(agreement);
        } else {
            LOGGER.error("Input object 'agreementRecord' is null");
            throw new IllegalArgumentException("Требуется указать договор для изменения");
        }
    }

    @Transactional
    public void deleteAgreement(long id) {
        if (id > 0) {
            LOGGER.info("Was invoked method to delete agreement");
            agreementRepository.deleteById(id);
        } else {
            LOGGER.error("Input id = " + id + " for deleting agreement is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id договора");
        }
    }

    @Transactional(readOnly = true)
    public List<AgreementRecord> findAllAgreements() {
        List<Agreement> agreementRecords = this.agreementRepository.findAll();
        if (!agreementRecords.isEmpty()) {
            LOGGER.info("Was invoked method to find all agreements");
            return agreementRecords.stream()
                    .map(this.modelMapper::mapToAgreementRecord)
                    .toList();
        } else {
            LOGGER.info("Was invoked method to find all agreements, but agreements were not found");
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Agreement> findAllAgreementsByDate(LocalDate localDate) {
        if (localDate != null) {
            List<Agreement> agreements = this.agreementRepository.findAll().stream()
                    .filter(e -> Objects.equals(e.getConclusionDate(), localDate))
                    .toList();
            if (!agreements.isEmpty()) {
                LOGGER.info("Was invoked method to find all agreements for the specified date = " + localDate);
                return agreements;
            } else {
                LOGGER.info("Was invoked method to find all agreements for the specified date = " +
                        localDate + ", but agreements were not found");
                return new ArrayList<>();
            }
        } else {
            LOGGER.error("Input object 'localDate' is null");
            throw new IllegalArgumentException("Требуется указать корректную дату");
        }
    }

    public Agreement findAgreementByCarerId(long carerId) {
        return agreementRepository.findAgreementByCarer_Id(carerId);
    }
}
