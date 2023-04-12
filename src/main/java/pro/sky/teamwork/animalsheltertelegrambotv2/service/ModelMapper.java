package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;

import java.time.LocalDate;

@Service
public class ModelMapper {
    /**
     * Метод занесения инфрмации в хранилище
     *
     * @param carerRecord
     * @return информацию внесенную через seagger
     */
    public Carer mapToCarerEntity(CarerRecord carerRecord) {
        Carer carer = new Carer();
        carer.setFullName(StringUtils.capitalize(carerRecord.getSecondName().toLowerCase()) + " " +
                StringUtils.capitalize(carerRecord.getFirstName().toLowerCase()) + " " +
                StringUtils.capitalize(carerRecord.getPatronymic().toLowerCase()));
        carer.setBirthYear(LocalDate.now().getYear() - carerRecord.getAge());
        carer.setPhoneNumber(carerRecord.getPhoneNumber());
        return carer;
    }

    /**
     * Метод занесения инфрмации в хранилище
     *
     * @param carer
     * @return информацию внесенную через телеграм
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
        return carerRecord;
    }

    /**
     * Метод занесения инфрмации в хранилище
     *
     * @param dogRecord
     * @return информацию внесенную через seagger
     */
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
     * Метод занесения инфрмации в хранилище
     *
     * @param dog
     * @return информацию внесенную через телеграм
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
}
