package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DogService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DogService.class);
    private final DogRepository dogRepository;
    private final ModelMapper modelMapper;

    public DogService(DogRepository dogRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Добавление информации о собаке в Swagger
     *
     * @param dogRecord {@link pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord}
     * @return данные по собаке добавлены
     * @throws IllegalArgumentException Если параметр <b>dogRecord</b> пустой.
     * @see DogRepository
     */
    @Transactional
    public DogRecord addDog(DogRecord dogRecord) {
        if (dogRecord != null) {
            LOGGER.info("Was invoked method for adding dog");
            Dog dog = this.dogRepository.save(
                    this.modelMapper.mapToDogEntity(dogRecord));
            return this.modelMapper.mapToDogRecord(dog);
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
     * @see org.springframework.data.jpa.repository.JpaRepository
     */
    @Transactional(readOnly = true)
    public DogRecord findDog(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for getting dog is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id собаки");
        }
        LOGGER.info("Was invoked method to find dog");
        Dog dog = this.dogRepository.findById(id).
                orElseThrow(DogNotFoundException::new);
        return this.modelMapper.mapToDogRecord(dog);
    }

    /**
     * Внесение изменений в информацию о собаке.
     *
     * @param dogRecord {@link pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord}
     * @return Информация по собаке изменена.
     * @throws IllegalArgumentException Если одно из полей {@link pro.sky.teamwork.animalsheltertelegrambotv2.dto.DogRecord} пустое.
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository
     */
    @Transactional
    public DogRecord editDog(DogRecord dogRecord) {
        if (dogRecord != null) {
            LOGGER.info("Was invoked method to edit dog");
            Dog dog = this.dogRepository.save(
                    this.modelMapper.mapToDogEntity(dogRecord));
            return this.modelMapper.mapToDogRecord(dog);
        } else {
            LOGGER.error("Input object 'dogRecord' is null");
            throw new IllegalArgumentException("Требуется указать собаку для изменения");
        }
    }

    /**
     * Удаление информации по собаке. Используется {@link org.springframework.data.jpa.repository.JpaRepository#deleteById(Object)}
     *
     * @param id идентификатор собаки.
     * @throws IllegalArgumentException При не верном указании id.
     * @see org.springframework.data.jpa.repository.JpaRepository
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DogRepository
     */
    @Transactional
    public void deleteDog(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for deleting dog is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id собаки");
        } else {
            LOGGER.info("Was invoked method to delete dog");
            this.dogRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public List<DogRecord> findAllDogs() {
        List<Dog> dogs = this.dogRepository.findAll();
        if (!dogs.isEmpty()) {
            LOGGER.info("Was invoked method to find all dogs");
            return dogs.stream()
                    .map(this.modelMapper::mapToDogRecord)
                    .toList();
        } else {
            LOGGER.info("Was invoked method to find all dogs, but dogs were not found");
            return new ArrayList<>();
        }
    }

    @Transactional
    public void changeIsTakenStatus(long id, boolean isTaken) {
        if (id > 0) {
            Dog dog = this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
            if (isTaken) {
                dog.setTaken(true);
                this.dogRepository.save(dog);
            } else {
                if (!dog.isOnProbation()) {
                    dog.setTaken(false);
                    this.dogRepository.save(dog);
                } else {
                    throw new IllegalArgumentException("Собака не может быть НЕ взята из приюта и на испытательном сроке");
                }
            }
        } else {
            throw new IllegalArgumentException("Требуется указать корректный id собаки");
        }
    }

    @Transactional
    public void changeOnProbationStatus(long id, boolean onProbation) {
        if (id > 0) {
            Dog dog = this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
            if (onProbation) {
                if (dog.isTaken()) {
                    dog.setOnProbation(true);
                    this.dogRepository.save(dog);
                } else {
                    throw new IllegalArgumentException("Собака не может быть НЕ взята из приюта и на испытательном сроке");
                }
            } else {
                dog.setOnProbation(false);
                this.dogRepository.save(dog);
            }
        } else {
            throw new IllegalArgumentException("Требуется указать корректный id собаки");
        }
    }
}
