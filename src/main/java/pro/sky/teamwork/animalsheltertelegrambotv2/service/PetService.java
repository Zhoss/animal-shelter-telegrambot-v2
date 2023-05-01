package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.PetRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Pet;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CatNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.DogNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PetService.class);
    private final DogRepository dogRepository;
    private final CatRepository catRepository;
    private final ModelMapper modelMapper;

    public PetService(DogRepository dogRepository, CatRepository catRepository, ModelMapper modelMapper) {
        this.dogRepository = dogRepository;
        this.catRepository = catRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Добавление информации о питомце в Swagger
     *
     * @param petRecord {@link PetRecord}
     * @return данные по питомцу добавлены
     * @throws IllegalArgumentException Если параметр <b>petRecord</b> пустой.
     * @see DogRepository
     */
    @Transactional
    public PetRecord addPet(PetRecord petRecord) {
        if (petRecord != null) {
            Pet pet = this.modelMapper.mapToPetEntity(petRecord);
            if (petRecord.getPetType() == PetType.CAT) {
                LOGGER.info("Was invoked method for adding cat");
                Cat cat = this.catRepository.save((Cat) pet);
                return this.modelMapper.mapToPetRecord(cat);
            } else if (petRecord.getPetType() == PetType.DOG) {
                LOGGER.info("Was invoked method for adding dog");
                Dog dog = this.dogRepository.save((Dog) pet);
                return this.modelMapper.mapToPetRecord(dog);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'petRecord' is null");
            throw new IllegalArgumentException("Требуется добавить питомца");
        }
    }

    /**
     * Поиск информации по питомцу.
     *
     * @param id через {@link DogRepository#findById(Object)}
     * @return Найденную информацию по питомцу.
     * @throws DogNotFoundException Если нет информации в БД.
     * @see org.springframework.data.jpa.repository.JpaRepository
     */
    @Transactional(readOnly = true)
    public PetRecord findPet(long id, PetType petType) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for getting pet is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id питомца");
        } else if (petType == PetType.CAT) {
            LOGGER.info("Was invoked method to find cat");
            Cat cat = this.catRepository.findById(id)
                    .orElseThrow(CatNotFoundException::new);
            return this.modelMapper.mapToPetRecord(cat);
        } else if (petType == PetType.DOG) {
            LOGGER.info("Was invoked method to find dog");
            Dog dog = this.dogRepository.findById(id)
                    .orElseThrow(DogNotFoundException::new);
            return this.modelMapper.mapToPetRecord(dog);
        } else {
            LOGGER.error("Wrong pet type");
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }

    /**
     * Внесение изменений в информацию о питомце.
     *
     * @param petRecord {@link PetRecord}
     * @return Информация по питомцу изменена.
     * @throws IllegalArgumentException Если одно из полей {@link PetRecord} пустое.
     * @see DogRepository
     */
    @Transactional
    public PetRecord editPet(PetRecord petRecord) {
        if (petRecord != null) {
            Pet pet = this.modelMapper.mapToPetEntity(petRecord);
            if (petRecord.getPetType() == PetType.CAT) {
                LOGGER.info("Was invoked method to edit cat");
                Cat cat = this.catRepository.save((Cat) pet);
                return this.modelMapper.mapToPetRecord(cat);
            } else if (petRecord.getPetType() == PetType.DOG) {
                LOGGER.info("Was invoked method to edit dog");
                Dog dog = this.dogRepository.save((Dog) pet);
                return this.modelMapper.mapToPetRecord(dog);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'petRecord' is null");
            throw new IllegalArgumentException("Требуется указать питомца для изменения");
        }
    }

    /**
     * Удаление информации по питомцу. Используется {@link org.springframework.data.jpa.repository.JpaRepository#deleteById(Object)}
     *
     * @param id идентификатор питомца.
     * @throws IllegalArgumentException При не верном указании id.
     * @see org.springframework.data.jpa.repository.JpaRepository
     * @see DogRepository
     */
    @Transactional
    public void deletePet(long id, PetType petType) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for deleting pet is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id питомца");
        } else {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to delete cat");
                this.catRepository.deleteById(id);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to delete dog");
                this.dogRepository.deleteById(id);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<PetRecord> findAllPets(PetType petType) {
        if (petType == PetType.CAT) {
            List<Cat> cats = this.catRepository.findAll();
            if (!cats.isEmpty()) {
                LOGGER.info("Was invoked method to find all cats");
                return cats.stream()
                        .map(this.modelMapper::mapToPetRecord)
                        .toList();
            } else {
                LOGGER.info("Was invoked method to find all cats, but cats were not found");
                return new ArrayList<>();
            }
        } else if (petType == PetType.DOG) {
            List<Dog> dogs = this.dogRepository.findAll();
            if (!dogs.isEmpty()) {
                LOGGER.info("Was invoked method to find all dogs");
                return dogs.stream()
                        .map(this.modelMapper::mapToPetRecord)
                        .toList();
            } else {
                LOGGER.info("Was invoked method to find all dogs, but dogs were not found");
                return new ArrayList<>();
            }
        } else {
            LOGGER.error("Wrong pet type");
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }

    @Transactional
    public void changeIsTakenStatus(long id, PetType petType, boolean isTaken) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to change \"is taken\" status of cat");
                Cat cat = this.catRepository.findById(id).orElseThrow(CatNotFoundException::new);
                if (isTaken) {
                    cat.setTaken(true);
                    this.catRepository.save(cat);
                } else {
                    if (!cat.isOnProbation()) {
                        cat.setTaken(false);
                        this.catRepository.save(cat);
                    } else {
                        LOGGER.error("Cat cannot NOT be taken from a shelter and on probation");
                        throw new IllegalArgumentException("Кошка не может быть НЕ взята из приюта и на испытательном сроке");
                    }
                }
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to change \"is taken\" status of dog");
                Dog dog = this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
                if (isTaken) {
                    dog.setTaken(true);
                    this.dogRepository.save(dog);
                } else {
                    if (!dog.isOnProbation()) {
                        dog.setTaken(false);
                        this.dogRepository.save(dog);
                    } else {
                        LOGGER.error("Dog cannot NOT be taken from a shelter and on probation");
                        throw new IllegalArgumentException("Собака не может быть НЕ взята из приюта и на испытательном сроке");
                    }
                }
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for deleting pet is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id питомца");
        }
    }

    @Transactional
    public void changeOnProbationStatus(long id, PetType petType, boolean onProbation) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to change \"on probation\" status of cat");
                Cat cat = this.catRepository.findById(id).orElseThrow(CatNotFoundException::new);
                if (onProbation) {
                    if (cat.isTaken()) {
                        cat.setOnProbation(true);
                        this.catRepository.save(cat);
                    } else {
                        LOGGER.error("Cat cannot NOT be taken from a shelter and on probation");
                        throw new IllegalArgumentException("Кошка не может быть НЕ взята из приюта и на испытательном сроке");
                    }
                } else {
                    cat.setOnProbation(false);
                    this.catRepository.save(cat);
                }
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to change \"on probation\" status of dog");
                Dog dog = this.dogRepository.findById(id).orElseThrow(DogNotFoundException::new);
                if (onProbation) {
                    if (dog.isTaken()) {
                        dog.setOnProbation(true);
                        this.dogRepository.save(dog);
                    } else {
                        LOGGER.error("Dog cannot NOT be taken from a shelter and on probation");
                        throw new IllegalArgumentException("Собака не может быть НЕ взята из приюта и на испытательном сроке");
                    }
                } else {
                    dog.setOnProbation(false);
                    this.dogRepository.save(dog);
                }
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for deleting pet is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id питомца");
        }
    }
}
