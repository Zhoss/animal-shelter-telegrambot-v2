package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CatNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Pet;

import java.time.LocalDate;

@Service
public class ModelMapper {
    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final DogCarerRepository dogCarerRepository;
    private final CatCarerRepository catCarerRepository;

    public ModelMapper(DogRepository dogRepository,
                       CatRepository catRepository,
                       DogCarerRepository dogCarerRepository,
                       CatCarerRepository catCarerRepository) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.dogCarerRepository = dogCarerRepository;
        this.catCarerRepository = catCarerRepository;
    }

    /**
     * Метод занесения информации в хранилище
     * <p>
     * //     * @param carer
     *
     * @return информацию, внесенную через телеграм
     */
    public CarerRecord mapToCarerRecord(Carer carer) {
        String[] fullName = carer.getFullName().split(" ");
        CarerRecord carerRecord = new CarerRecord();
        carerRecord.setId(carer.getId());
        carerRecord.setSecondName(fullName[0]);
        carerRecord.setFirstName(fullName[1]);
        carerRecord.setPatronymic(fullName[2]);
        carerRecord.setAge(LocalDate.now().getYear() - carer.getBirthYear());
        carerRecord.setPhoneNumber(carer.getPhoneNumber());
        carerRecord.setPassportNumber(carer.getPassportNumber());
        if (carer.getClass().equals(CatCarer.class)) {
            carerRecord.setPetType("кошка");
            CatCarer catCarer = (CatCarer) carer;
            Cat cat = catCarer.getCat();
            if (cat != null) {
                carerRecord.setPetId(catCarer.getCat().getId());
            } else {
                carerRecord.setPetId(0L);
            }
        } else if (carer.getClass().equals(DogCarer.class)) {
            carerRecord.setPetType("собака");
            DogCarer dogCarer = (DogCarer) carer;
            Dog dog = dogCarer.getDog();
            if (dog != null) {
                carerRecord.setPetId(dogCarer.getDog().getId());
            } else {
                carerRecord.setPetId(0L);
            }
        }
        return carerRecord;
    }

    public void updateCarer(CarerRecord carerRecord, Carer carer) {
        if (carerRecord.getPetType().equals("кошка")) {
            CatCarer catCarer = (CatCarer) carer;
            catCarer.setId(carerRecord.getId());
            catCarer.setFullName(StringUtils.capitalize(carerRecord.getSecondName().toLowerCase()) + " " +
                    StringUtils.capitalize(carerRecord.getFirstName().toLowerCase()) + " " +
                    StringUtils.capitalize(carerRecord.getPatronymic().toLowerCase()));
            catCarer.setBirthYear(LocalDate.now().getYear() - carerRecord.getAge());
            catCarer.setPhoneNumber(carerRecord.getPhoneNumber());
            catCarer.setPassportNumber(carerRecord.getPassportNumber());
            Cat cat = catRepository.findById(carerRecord.getPetId())
                    .orElseThrow(CatNotFoundException::new);
            catCarer.setCat(cat);
        } else if (carerRecord.getPetType().equals("собака")) {
            DogCarer dogCarer = (DogCarer) carer;
            dogCarer.setId(carerRecord.getId());
            dogCarer.setFullName(StringUtils.capitalize(carerRecord.getSecondName().toLowerCase()) + " " +
                    StringUtils.capitalize(carerRecord.getFirstName().toLowerCase()) + " " +
                    StringUtils.capitalize(carerRecord.getPatronymic().toLowerCase()));
            dogCarer.setBirthYear(LocalDate.now().getYear() - carerRecord.getAge());
            dogCarer.setPhoneNumber(carerRecord.getPhoneNumber());
            dogCarer.setPassportNumber(carerRecord.getPassportNumber());
            Dog dog = dogRepository.findById(carerRecord.getPetId())
                    .orElseThrow(DogNotFoundException::new);
            dogCarer.setDog(dog);
        }
    }

    public Pet mapToPetEntity(PetRecord petRecord) {
        if (petRecord.getPetType().equals("кошка")) {
            Cat cat = new Cat();
            cat.setName(StringUtils.capitalize(petRecord.getName().toLowerCase()));
            cat.setBreed(petRecord.getBreed());
            cat.setCoatColor(petRecord.getCoatColor());
            cat.setAge(petRecord.getAge());
            cat.setFeatures(petRecord.getFeatures());
            return cat;
        } else if (petRecord.getPetType().equals("собака")) {
            Dog dog = new Dog();
            dog.setName(StringUtils.capitalize(petRecord.getName().toLowerCase()));
            dog.setBreed(petRecord.getBreed());
            dog.setCoatColor(petRecord.getCoatColor());
            dog.setAge(petRecord.getAge());
            dog.setFeatures(petRecord.getFeatures());
            return dog;
        }
        throw new IllegalArgumentException("Тип животного указан не верно");
    }


    /**
     * Метод занесения информации в хранилище
     *
     * @param pet
     * @return информацию, внесенную через телеграм
     */
    public PetRecord mapToPetRecord(Pet pet) {
        PetRecord petRecord = new PetRecord();
        petRecord.setId(pet.getId());
        petRecord.setName(pet.getName());
        petRecord.setBreed(pet.getBreed());
        petRecord.setCoatColor(pet.getCoatColor());
        petRecord.setAge(pet.getAge());
        petRecord.setFeatures(pet.getFeatures());
        if (pet.getClass().equals(Cat.class)) {
            petRecord.setPetType("кошка");
        } else if (pet.getClass().equals(Dog.class)) {
            petRecord.setPetType("собака");
        }
        return petRecord;
    }

    public Agreement mapToAgreementEntity(AgreementRecord agreementRecord) {
        if (agreementRecord.getPetType().equals("кошка")) {
            CatAgreement catAgreement = new CatAgreement();
            catAgreement.setId(agreementRecord.getId());
            catAgreement.setNumber(agreementRecord.getNumber());
            catAgreement.setConclusionDate(agreementRecord.getConclusionDate());
            catAgreement.setProbationEndData(agreementRecord.getConclusionDate().plusDays(30));
            CatCarer catCarer = this.catCarerRepository.findById(agreementRecord.getCarerId())
                    .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
            catAgreement.setCarer(catCarer);
            return catAgreement;
        } else if (agreementRecord.getPetType().equals("собака")) {
            DogAgreement dogAgreement = new DogAgreement();
            dogAgreement.setId(agreementRecord.getId());
            dogAgreement.setNumber(agreementRecord.getNumber());
            dogAgreement.setConclusionDate(agreementRecord.getConclusionDate());
            dogAgreement.setProbationEndData(agreementRecord.getConclusionDate().plusDays(30));
            DogCarer dogCarer = this.dogCarerRepository.findById(agreementRecord.getCarerId())
                    .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
            dogAgreement.setCarer(dogCarer);
            return dogAgreement;
        } else {
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }

    public AgreementRecord mapToAgreementRecord(Agreement agreement) {
        AgreementRecord agreementRecord = new AgreementRecord();
        agreementRecord.setId(agreement.getId());
        agreementRecord.setNumber(agreement.getNumber());
        agreementRecord.setConclusionDate(agreement.getConclusionDate());
        agreementRecord.setProbationEndData(agreement.getProbationEndData());
        if (agreement.getClass().equals(CatAgreement.class)) {
            agreementRecord.setPetType("кошка");
            CatAgreement catAgreement = (CatAgreement) agreement;
            agreementRecord.setCarerId(catAgreement.getCarer().getId());
        } else if (agreement.getClass().equals(DogAgreement.class)) {
            agreementRecord.setPetType("собака");
            DogAgreement dogAgreement = (DogAgreement) agreement;
            agreementRecord.setCarerId(dogAgreement.getCarer().getId());
        }
        return agreementRecord;
    }

    public DailyReportRecord mapToDailyRecordRecord(DailyReport dailyReport) {
        DailyReportRecord dailyReportRecord = new DailyReportRecord();
        dailyReportRecord.setId(dailyReport.getId());
        dailyReportRecord.setReportDate(dailyReport.getReportDate());
        dailyReportRecord.setDogDiet(dailyReport.getDogDiet());
        dailyReportRecord.setDogHealth(dailyReport.getDogHealth());
        dailyReportRecord.setDogBehavior(dailyReport.getDogBehavior());
        if (dailyReport.getClass().equals(CatDailyReport.class)) {
            dailyReportRecord.setPetType("кошка");
            CatDailyReport catDailyReport = (CatDailyReport) dailyReport;
            dailyReportRecord.setCarerId(catDailyReport.getCarer().getId());
        } else if (dailyReport.getClass().equals(DogDailyReport.class)) {
            dailyReportRecord.setPetType("собака");
            DogDailyReport dogDailyReport = (DogDailyReport) dailyReport;
            dailyReportRecord.setCarerId(dogDailyReport.getCarer().getId());
        }
        return dailyReportRecord;
    }
}
