package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

@Service
public class DogService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DogService.class);
    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    /**
     * Добавление информации о собаке в Swagger
     *
     * @param dogRecord {@link pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord}
     * @return данные по собаке добавлены
     * @throws IllegalArgumentException Если параметр <b>dogRecord</b> пустой.
     *
     * @see DogRepository
     */
    @Transactional
    public Dog addDog(DogRecord dogRecord) {
        if (dogRecord != null) {
            Dog dog = new Dog();
            dog.setName(dogRecord.getName());
            dog.setBreed(dogRecord.getBreed());
            dog.setCoatColor(dogRecord.getCoatColor());
            dog.setAge(dogRecord.getAge());
            dog.setFeatures(dogRecord.getFeatures());
            LOGGER.info("Was invoked method for adding dog");
            return this.dogRepository.save(dog);
        } else {
            LOGGER.error("Input object 'dogRecord' is null");
            throw new IllegalArgumentException("Требуется добавить собаку");
        }
    }

    /**
     * Поиск информации по собаке.
     *
     * @param id через {@link pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository#findById(Object)}
     * @return Найденную информацию по собаке.
     * @throws DogNotFoundException Если нет информации в БД.
     *
     * @see org.springframework.data.jpa.repository.JpaRepository
     */

    @Transactional
    public Dog findDog(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for getting dog is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id собаки");
        }
        LOGGER.info("Was invoked method to find dog");
        return this.dogRepository.findById(id).
                orElseThrow(DogNotFoundException::new);
    }

    /**
     * Внесение изменений в информацию о собаке.
     *
     * @param dogRecord {@link pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord}
     * @return Информация по собаке изменена.
     * @throws IllegalArgumentException Если одно из полей {@link pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord} пустое.
     *
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository
     */
    @Transactional
    public Dog editDog(DogRecord dogRecord) {
        if (dogRecord != null) {
            Dog dog = new Dog();
            dog.setName(dogRecord.getName());
            dog.setBreed(dogRecord.getBreed());
            dog.setCoatColor(dogRecord.getCoatColor());
            dog.setAge(dogRecord.getAge());
            dog.setFeatures(dogRecord.getFeatures());
            LOGGER.info("Was invoked method to edit dog");
            return this.dogRepository.save(dog);
        } else {
            LOGGER.error("Input object 'dogRecord' is null");
            throw new IllegalArgumentException("Требуется добавить собаку");
        }
    }
    /**
     * Удаление информации по собаке. Используется {@link org.springframework.data.jpa.repository.JpaRepository#deleteById(Object)}
     * @param id идентификатор собаки.
     *
     * @throws IllegalArgumentException При не верном указании id.
     *
     * @see org.springframework.data.jpa.repository.JpaRepository
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository
     */
    public void deleteDog(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for deleting dog is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id собаки");
        } else {
            LOGGER.info("Was invoked method to delete dog");
            this.dogRepository.deleteById(id);
        }
    }
}
