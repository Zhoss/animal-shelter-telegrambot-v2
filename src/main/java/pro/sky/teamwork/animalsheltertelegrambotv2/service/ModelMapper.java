package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.AgreementRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

import java.time.LocalDate;

@Service
public class ModelMapper {
    private final DogRepository dogRepository;

    public ModelMapper(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    private void mapToCarer(CarerRecord carerRecord, Carer carer) {
        carer.setId(carerRecord.getId());
        carer.setFullName(StringUtils.capitalize(carerRecord.getSecondName().toLowerCase()) + " " +
                StringUtils.capitalize(carerRecord.getFirstName().toLowerCase()) + " " +
                StringUtils.capitalize(carerRecord.getPatronymic().toLowerCase()));
        carer.setBirthYear(LocalDate.now().getYear() - carerRecord.getAge());
        carer.setPhoneNumber(carerRecord.getPhoneNumber());
        carer.setPassportNumber(carerRecord.getPassportNumber());
        Dog dog = dogRepository.findById(carerRecord.getDogId()).orElseThrow(DogNotFoundException::new);
        carer.setDog(dog);
    }

    public Carer mapToCarerEntity(CarerRecord carerRecord) {
        Carer carer = new Carer();
        mapToCarer(carerRecord, carer);
        return carer;
    }

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
        mapToCarer(carerRecord, carer);
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
//        agreement.setId(1);
        agreement.setNumber(agreementRecord.getNumber());
        agreement.setConclusionDate(agreementRecord.getConclusionDate());
        agreement.setCarer(agreementRecord.getCarer());
        return agreement;
    }

    public AgreementRecord mapToAgreementRecord(Agreement agreement) {
        AgreementRecord agreementRecord = new AgreementRecord();
        agreementRecord.setNumber(agreement.getNumber());
        agreementRecord.setConclusionDate(agreement.getConclusionDate());
        agreementRecord.setCarer(agreement.getCarer());
        return agreementRecord;
    }
}
