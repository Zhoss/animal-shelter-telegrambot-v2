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
    private final ModelMapper modelMapper;

    public DogService(DogRepository dogRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public DogRecord addDog(DogRecord dogRecord) {
        if (dogRecord != null) {
            LOGGER.info("Was invoked method for adding dog");
            Dog dog = this.dogRepository.save(this.modelMapper.mapToDogEntity(dogRecord));
            return this.modelMapper.mapToDogRecord(dog);
        } else {
            LOGGER.error("Input object 'dogRecord' is null");
            throw new IllegalArgumentException("Требуется добавить собаку");
        }
    }

    @Transactional
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

    @Transactional
    public DogRecord editDog(DogRecord dogRecord) {
        if (dogRecord != null) {
            LOGGER.info("Was invoked method to edit dog");
            Dog dog = this.dogRepository.save(this.modelMapper.mapToDogEntity(dogRecord));
            return this.modelMapper.mapToDogRecord(dog);
        } else {
            LOGGER.error("Input object 'dogRecord' is null");
            throw new IllegalArgumentException("Требуется добавить собаку");
        }
    }

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
