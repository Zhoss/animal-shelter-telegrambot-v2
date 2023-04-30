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
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

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
            if (agreementRecord.getPetType() == PetType.CAT) {
                CatAgreement catAgreement = this.catAgreementRepository.save((CatAgreement) agreement);
                Cat cat = catAgreement.getCarer().getCat();
                cat.setTaken(true);
                cat.setOnProbation(true);
                this.catRepository.save(cat);
                LOGGER.info("Was invoked method for adding cat agreement");
                return modelMapper.mapToAgreementRecord(catAgreement);
            } else if (agreementRecord.getPetType() == PetType.DOG) {
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
    public AgreementRecord findAgreementById(long id, PetType petType) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                CatAgreement catAgreement = catAgreementRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
                LOGGER.info("Was invoked method to find cat agreement by id");
                return modelMapper.mapToAgreementRecord(catAgreement);
            } else if (petType == PetType.DOG) {
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
            if (agreementRecord.getPetType() == PetType.CAT) {
                LOGGER.info("Was invoked method to edit cat agreement");
                CatAgreement catAgreement = this.catAgreementRepository.
                        save((CatAgreement) agreement);
                return modelMapper.mapToAgreementRecord(catAgreement);
            } else if (agreementRecord.getPetType() == PetType.DOG) {
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
    public void deleteAgreement(long id, PetType petType) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to delete cat agreement");
                catAgreementRepository.deleteById(id);
            } else if (petType == PetType.DOG) {
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
    public List<AgreementRecord> findAllAgreements(PetType petType) {
        if (petType == PetType.CAT) {
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
        } else if (petType == PetType.DOG) {
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

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends Agreement> List<T> findAllAgreementsWithProbationByDate(LocalDate localDate, PetType petType) {
        if (localDate != null) {
            if (petType == PetType.CAT) {
                List<CatAgreement> catAgreements = this.catAgreementRepository.findAll().stream()
                        .filter(e -> localDate.isBefore(e.getProbationEndData()))
                        .toList();
                if (!catAgreements.isEmpty()) {
                    LOGGER.info("Was invoked method to find all cat agreements with probation for the specified date = " +
                            localDate);
                    return (List<T>) catAgreements;
                } else {
                    LOGGER.info("Was invoked method to find all cat agreements with probation for the specified date = " +
                            localDate + ", but cat agreements were not found");
                    return new ArrayList<>();
                }
            } else if (petType == PetType.DOG) {
                List<DogAgreement> dogAgreements = this.dogAgreementRepository.findAll().stream()
                        .filter(e -> localDate.isBefore(e.getProbationEndData()))
                        .toList();
                if (!dogAgreements.isEmpty()) {
                    LOGGER.info("Was invoked method to find all dog agreements with probation for the specified date = " +
                            localDate);
                    return (List<T>) dogAgreements;
                } else {
                    LOGGER.info("Was invoked method to find all dog agreements with probation for the specified date = " +
                            localDate + ", but dog agreements were not found");
                    return new ArrayList<>();
                }
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'localDate' is null");
            throw new IllegalArgumentException("Требуется указать корректную дату");
        }
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public <T extends Agreement> List<T> findAgreementsWithEndingProbation(PetType petType) {
        if (petType == PetType.CAT) {
            LOGGER.info("Was invoked method to find all agreements from Timer");
            return (List<T>) this.catAgreementRepository.findAll().stream()
                    .filter(e -> e.getProbationEndData().minusDays(2).equals(LocalDate.now()))
                    .toList();
        } else if (petType == PetType.DOG) {
            LOGGER.info("Was invoked method to find all agreements from Timer");
            return (List<T>) this.dogAgreementRepository.findAll().stream()
                    .filter(e -> e.getProbationEndData().minusDays(2).equals(LocalDate.now()))
                    .toList();
        } else {
            LOGGER.error("Wrong pet type");
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }

    public AgreementRecord changeProbationEndData(long id, LocalDate localDate, PetType petType) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                CatAgreement catAgreement = this.catAgreementRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
                catAgreement.setProbationEndData(localDate);
                this.catAgreementRepository.save(catAgreement);
                return this.modelMapper.mapToAgreementRecord(catAgreement);
            } else if (petType == PetType.DOG) {
                DogAgreement dogAgreement = this.dogAgreementRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Договор не найден"));
                dogAgreement.setProbationEndData(localDate);
                this.dogAgreementRepository.save(dogAgreement);
                return this.modelMapper.mapToAgreementRecord(dogAgreement);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for changing agreement end probation date is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id договора");
        }
    }
}
