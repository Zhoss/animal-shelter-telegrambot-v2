package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.CarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

import java.time.LocalDate;

@Service
public class ModelMapper {
    private final DogRepository dogRepository;
    private final CarerRepository carerRepository;

    public ModelMapper(DogRepository dogRepository, CarerRepository carerRepository) {
        this.dogRepository = dogRepository;
        this.carerRepository = carerRepository;
    }

    /**
     * Метод занесения инфрмации в хранилище
     *
     * @param carer
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
        carerRecord.setDogId(carer.getDog().getId());
        return carerRecord;
    }

    public void updateCarer(CarerRecord carerRecord, Carer carer) {
        carer.setId(carerRecord.getId());
        carer.setFullName(StringUtils.capitalize(carerRecord.getSecondName().toLowerCase()) + " " +
                StringUtils.capitalize(carerRecord.getFirstName().toLowerCase()) + " " +
                StringUtils.capitalize(carerRecord.getPatronymic().toLowerCase()));
        carer.setBirthYear(LocalDate.now().getYear() - carerRecord.getAge());
        carer.setPhoneNumber(carerRecord.getPhoneNumber());
        carer.setPassportNumber(carerRecord.getPassportNumber());
        Dog dog = dogRepository.findById(carerRecord.getDogId())
                .orElseThrow(DogNotFoundException::new);
        carer.setDog(dog);
    }

    public Dog mapToDogEntity(DogRecord dogRecord) {
        Dog dog = new Dog();
        dog.setName(StringUtils.capitalize(dogRecord.getName().toLowerCase()));
        dog.setBreed(dogRecord.getBreed());
        dog.setCoatColor(dogRecord.getCoatColor());
        dog.setAge(dogRecord.getAge());
        dog.setFeatures(dogRecord.getFeatures());
        return dog;
    }

    /**
     * Метод занесения информации в хранилище
     *
     * @param dog
     * @return информацию, внесенную через телеграм
     */
    public DogRecord mapToDogRecord(Dog dog) {
        DogRecord dogRecord = new DogRecord();
        dogRecord.setId(dog.getId());
        dogRecord.setName(dog.getName());
        dogRecord.setBreed(dog.getBreed());
        dogRecord.setCoatColor(dog.getCoatColor());
        dogRecord.setAge(dog.getAge());
        dogRecord.setFeatures(dog.getFeatures());
        return dogRecord;
    }

    public Agreement mapToAgreementEntity(AgreementRecord agreementRecord) {
        Agreement agreement = new Agreement();
        agreement.setId(agreementRecord.getId());
        agreement.setNumber(agreementRecord.getNumber());
        agreement.setConclusionDate(agreementRecord.getConclusionDate());
        Carer carer = this.carerRepository.findById(agreementRecord.getCarerId())
                .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
        agreement.setCarer(carer);
        return agreement;
    }

    public AgreementRecord mapToAgreementRecord(Agreement agreement) {
        AgreementRecord agreementRecord = new AgreementRecord();
        agreementRecord.setId(agreement.getId());
        agreementRecord.setNumber(agreement.getNumber());
        agreementRecord.setConclusionDate(agreement.getConclusionDate());
        agreementRecord.setCarerId(agreement.getCarer().getId());
        return agreementRecord;
    }

    public DailyReportRecord mapToDailyRecordRecord(DailyReport dailyReport) {
        DailyReportRecord dailyReportRecord = new DailyReportRecord();
        dailyReportRecord.setId(dailyReport.getId());
        dailyReportRecord.setReportDate(dailyReport.getReportDate());
        dailyReportRecord.setDogDiet(dailyReport.getDogDiet());
        dailyReportRecord.setDogHealth(dailyReport.getDogHealth());
        dailyReportRecord.setDogBehavior(dailyReport.getDogBehavior());
        dailyReportRecord.setCarerId(dailyReport.getCarer().getId());
        return dailyReportRecord;
    }
}
