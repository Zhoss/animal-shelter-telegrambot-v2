package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogAgreementRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgreementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportService.class);
    private final DogAgreementRepository dogAgreementRepository;
    private final CatAgreementRepository catAgreementRepository;
    private final ModelMapper modelMapper;
    private final DogRepository dogRepository;
    private final CatRepository catRepository;

    public AgreementService(DogAgreementRepository dogAgreementRepository,
                            CatAgreementRepository catAgreementRepository, ModelMapper modelMapper,
                            DogRepository dogRepository, CatRepository catRepository) {
        this.dogAgreementRepository = dogAgreementRepository;
        this.catAgreementRepository = catAgreementRepository;
        this.modelMapper = modelMapper;
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
    }

    @Transactional
    public AgreementRecord addAgreement(AgreementRecord agreementRecord) {
        if (agreementRecord != null) {
            Agreement agreement = this.modelMapper.mapToAgreementEntity(agreementRecord);
            if (agreementRecord.getPetType().equals("кошка")) {
                CatAgreement catAgreement = this.catAgreementRepository.save((CatAgreement) agreement);
                Cat cat = catAgreement.getCarer().getCat();
                cat.setTaken(true);
                cat.setOnProbation(true);
                this.catRepository.save(cat);
                LOGGER.info("Was invoked method for adding cat agreement");
                return modelMapper.mapToAgreementRecord(catAgreement);
            } else if (agreementRecord.getPetType().equals("собака")) {
                DogAgreement dogAgreement = this.dogAgreementRepository.save((DogAgreement) agreement);
                Dog dog = dogAgreement.getCarer().getDog();
                dog.setTaken(true);
                dog.setOnProbation(true);
                this.dogRepository.save(dog);
                LOGGER.info("Was invoked method for adding dog agreement");
                return modelMapper.mapToAgreementRecord(dogAgreement);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'agreementRecord' is null");
            throw new IllegalArgumentException("Требуется добавить договор");
        }
    }

    @Transactional(readOnly = true)
    public AgreementRecord findAgreementById(long id, String petType) {
        if (id > 0) {
            if (petType.equals("кошка")) {
                CatAgreement catAgreement = catAgreementRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
                LOGGER.info("Was invoked method to find cat agreement by id");
                return modelMapper.mapToAgreementRecord(catAgreement);
            } else if (petType.equals("собака")) {
                DogAgreement dogAgreement = dogAgreementRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
                LOGGER.info("Was invoked method to find dog agreement by id");
                return modelMapper.mapToAgreementRecord(dogAgreement);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for getting agreement is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id договора");
        }
    }

    @Transactional
    public AgreementRecord editAgreement(AgreementRecord agreementRecord) {
        if (agreementRecord != null) {
            Agreement agreement = this.modelMapper.mapToAgreementEntity(agreementRecord);
            if (agreementRecord.getPetType().equals("кошка")) {
                LOGGER.info("Was invoked method to edit cat agreement");
                CatAgreement catAgreement = this.catAgreementRepository.save((CatAgreement) agreement);
                return modelMapper.mapToAgreementRecord(catAgreement);
            } else if (agreementRecord.getPetType().equals("собака")) {
                LOGGER.info("Was invoked method to edit dog agreement");
                DogAgreement dogAgreement = this.dogAgreementRepository.save((DogAgreement) agreement);
                return modelMapper.mapToAgreementRecord(dogAgreement);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'agreementRecord' is null");
            throw new IllegalArgumentException("Требуется указать договор для изменения");
        }
    }

    @Transactional
    public void deleteAgreement(long id, String petType) {
        if (id > 0) {
            if (petType.equals("кошка")) {
                LOGGER.info("Was invoked method to delete cat agreement");
                catAgreementRepository.deleteById(id);
            } else if (petType.equals("собака")) {
                LOGGER.info("Was invoked method to delete dog agreement");
                dogAgreementRepository.deleteById(id);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for deleting agreement is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id договора");
        }
    }

    @Transactional(readOnly = true)
    public List<AgreementRecord> findAllAgreements(String petType) {
        if (petType.equals("кошка")) {
            List<CatAgreement> catAgreements = this.catAgreementRepository.findAll();
            if (!catAgreements.isEmpty()) {
                LOGGER.info("Was invoked method to find all cat agreements");
                return catAgreements.stream()
                        .map(this.modelMapper::mapToAgreementRecord)
                        .toList();
            } else {
                LOGGER.info("Was invoked method to find all cat agreements, but cat agreements were not found");
                return new ArrayList<>();
            }
        } else if (petType.equals("собака")) {
            List<DogAgreement> dogAgreements = this.dogAgreementRepository.findAll();
            if (!dogAgreements.isEmpty()) {
                LOGGER.info("Was invoked method to find all agreements");
                return dogAgreements.stream()
                        .map(this.modelMapper::mapToAgreementRecord)
                        .toList();
            } else {
                LOGGER.info("Was invoked method to find all agreements, but agreements were not found");
                return new ArrayList<>();
            }
        } else {
            LOGGER.error("Wrong pet type");
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }

//    @Transactional(readOnly = true)
//    public List<Agreement> findAllAgreementsWithProbationByDate(LocalDate localDate) {
//        if (localDate != null) {
//            List<Agreement> dogAgreements = this.dogAgreementRepository.findAll().stream()
//                    .filter(e -> localDate.isBefore(e.getProbationEndData()))
//                    .toList();
//            if (!dogAgreements.isEmpty()) {
//                LOGGER.info("Was invoked method to find all agreements with probation for the specified date = " +
//                        localDate);
//                return dogAgreements;
//            } else {
//                LOGGER.info("Was invoked method to find all agreements with probation for the specified date = " +
//                        localDate + ", but agreements were not found");
//                return new ArrayList<>();
//            }
//        } else {
//            LOGGER.error("Input object 'localDate' is null");
//            throw new IllegalArgumentException("Требуется указать корректную дату");
//        }
//    }
//
//    @Transactional(readOnly = true)
//    public List<Agreement> findAgreements() {
//        LOGGER.info("Was invoked method to find all agreements from Timer");
//        return this.dogAgreementRepository.findAll();
//    }
}
