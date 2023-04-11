package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;

import java.time.LocalDate;

@Service
public class ModelMapper {
    public Carer mapToCarerEntity(CarerRecord carerRecord) {
        Carer carer = new Carer();
        carer.setFullName(carerRecord.getSecondName() + " " +
                carerRecord.getFirstName() + " " +
                carerRecord.getPatronymic());
        carer.setBirthYear(LocalDate.now().getYear() - carerRecord.getAge());
        carer.setPhoneNumber(carerRecord.getPhoneNumber());
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
        carer.setPhoneNumber(carer.getPhoneNumber());
        return carerRecord;
    }

    public Dog mapToDogEntity(DogRecord dogRecord) {
        Dog dog = new Dog();
        dog.setName(dogRecord.getName());
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
}
