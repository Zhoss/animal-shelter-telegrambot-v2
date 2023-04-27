package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository.CatCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository.DogCarerRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CarerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarerService.class);
    private static final Pattern CARER_NUMBER_PATTERN = Pattern.compile("^(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})$");
    private final DogCarerRepository dogCarerRepository;
    private final CatCarerRepository catCarerRepository;
    private final ModelMapper modelMapper;

    public CarerService(DogCarerRepository dogCarerRepository, CatCarerRepository catCarerRepository, ModelMapper modelMapper) {
        this.dogCarerRepository = dogCarerRepository;
        this.catCarerRepository = catCarerRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Добавление информации по опекуну через телеграм бота.
     *
     * @param fullName    {@link DogCarer#setFullName(String)}
     * @param age         {@link DogCarer#setBirthYear(int)} - преобразовывает полученную дату рождения в возраст.
     * @param phoneNumber {@link DogCarer#setPhoneNumber(String)}
     * @return данные по опекуну добавлены
     * @throws IllegalArgumentException Если же поля данных: Имя и телефон пустые
     * @see DogCarerRepository
     */
    @Transactional
    public Carer addCarer(String fullName, int age, String phoneNumber, long chatId, PetType petType) {
        if (!fullName.isEmpty() && !fullName.isBlank() &&
                !phoneNumber.isEmpty() && !phoneNumber.isBlank()) {
            if (petType == PetType.CAT) {
                CatCarer catCarer = new CatCarer();
                catCarer.setFullName(fullName);
                catCarer.setBirthYear(LocalDate.now().getYear() - age);
                catCarer.setPhoneNumber(phoneNumber);
                catCarer.setChatId(chatId);
                LOGGER.info("Was invoked method for adding cat carer from Telegram bot");
                this.catCarerRepository.save(catCarer);
                return catCarer;
            } else if (petType == PetType.DOG) {
                DogCarer dogCarer = new DogCarer();
                dogCarer.setFullName(fullName);
                dogCarer.setBirthYear(LocalDate.now().getYear() - age);
                dogCarer.setPhoneNumber(phoneNumber);
                dogCarer.setChatId(chatId);
                LOGGER.info("Was invoked method for adding dog carer from Telegram bot");
                this.dogCarerRepository.save(dogCarer);
                return dogCarer;
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Carer's full name or phone number is empty");
            throw new IllegalArgumentException("Требуется указать корректные данные: имя опекуна, телефонный номер опекуна");
        }
    }

    @Transactional
    public Carer saveCarer(Carer carer, PetType petType) {
        if (carer != null) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method for saving cat carer from Telegram bot");
                return this.catCarerRepository.save((CatCarer) carer);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method for saving dog carer from Telegram bot");
                return this.dogCarerRepository.save((DogCarer) carer);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'carer' is null");
            throw new IllegalArgumentException("Требуется добавить опекуна");
        }
    }

    /**
     * Поиск информации по опекуну через id. Используется {@link org.springframework.data.jpa.repository.JpaRepository#findById(Object)}
     *
     * @param id - идентификационный номер опекуна
     * @return найдена информация по опекуну
     * @throws CarerNotFoundException если опекун с таким идентификационным номером (id) не найден
     * @see org.springframework.data.jpa.repository.JpaRepository#findById(Object)
     */
    @Transactional(readOnly = true)
    public CarerRecord findCarer(long id, PetType petType) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to find cat carer");
                CatCarer catCarer = this.catCarerRepository.findById(id).
                        orElseThrow(() -> new CarerNotFoundException("Опекун с id = " + id + " не найден"));
                return this.modelMapper.mapToCarerRecord(catCarer);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to find dog carer");
                DogCarer dogCarer = this.dogCarerRepository.findById(id).
                        orElseThrow(() -> new CarerNotFoundException("Опекун с id = " + id + " не найден"));
                return this.modelMapper.mapToCarerRecord(dogCarer);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for getting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        }
    }

    /**
     * Внесение изменений в информацию <b>опекуна</b>
     * //     * @param carerRecord класс DTO
     *
     * @return измененная информация об опекуне.
     * @throws IllegalArgumentException Если поля <b>carerRecord</b> пустые (null)
     * @see CarerRecord
     */
    @Transactional(readOnly = true)
    public Carer findCarerByChatId(long chatId, PetType petType) {
        if (chatId != 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to find cat carer by chat id from Telegram");
                return this.catCarerRepository.findCatCarerByChatId(chatId)
                        .orElse(null);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to find dog carer by chat id from Telegram");
                return this.dogCarerRepository.findDogCarerByChatId(chatId)
                        .orElse(null);
            } else {
                return null;
            }
        } else {
            LOGGER.error("Input chat id = " + chatId + " for getting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id чата опекуна");
        }
    }

    @Transactional
    public CarerRecord editCarer(CarerRecord carerRecord) {
        if (carerRecord != null) {
            if (carerRecord.getPetType() == PetType.CAT) {
                LOGGER.info("Was invoked method to edit cat carer");
                CatCarer catCarer = this.catCarerRepository.findById(carerRecord.getId())
                        .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
                this.modelMapper.updateCarer(carerRecord, catCarer);
                this.catCarerRepository.save(catCarer);
                return this.modelMapper.mapToCarerRecord(catCarer);
            } else if (carerRecord.getPetType() == PetType.DOG) {
                LOGGER.info("Was invoked method to edit dog carer");
                DogCarer dogCarer = this.dogCarerRepository.findById(carerRecord.getId())
                        .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
                this.modelMapper.updateCarer(carerRecord, dogCarer);
                this.dogCarerRepository.save(dogCarer);
                return this.modelMapper.mapToCarerRecord(dogCarer);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input object 'carerRecord' is null");
            throw new IllegalArgumentException("Требуется указать опекуна для изменения");
        }
    }


    /**
     * Удаление информации по опекуну. Используется {@link org.springframework.data.jpa.repository.JpaRepository#deleteById(Object)}
     *
     * @param id идентификатор опекуна
     * @throws IllegalArgumentException При не верном указании id.
     * @see org.springframework.data.jpa.repository.JpaRepository#deleteById(Object)
     */
    @Transactional
    public void deleteCarer(long id, PetType petType) {
        if (id > 0) {
            if (petType == PetType.CAT) {
                LOGGER.info("Was invoked method to delete cat carer");
                this.catCarerRepository.deleteById(id);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Was invoked method to delete dog carer");
                this.dogCarerRepository.deleteById(id);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input id = " + id + " for deleting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        }
    }

    @Transactional(readOnly = true)
    public CarerRecord findCarerByPhoneNumber(String phoneNumber, PetType petType) {
        Matcher matcher = CARER_NUMBER_PATTERN.matcher(phoneNumber);
        if (matcher.matches()) {
            if (petType == PetType.CAT) {
                LOGGER.info("Getting cat carer by his phone number");
                CatCarer catCarer = this.catCarerRepository.findCatCarerByPhoneNumber(phoneNumber)
                        .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
                return this.modelMapper.mapToCarerRecord(catCarer);
            } else if (petType == PetType.DOG) {
                LOGGER.info("Getting dog carer by his phone number");
                DogCarer dogCarer = this.dogCarerRepository.findDogCarerByPhoneNumber(phoneNumber)
                        .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
                return this.modelMapper.mapToCarerRecord(dogCarer);
            } else {
                LOGGER.error("Wrong pet type");
                throw new IllegalArgumentException("Тип животного указан не верно");
            }
        } else {
            LOGGER.error("Input phone number = " + phoneNumber + " is incorrect");
            throw new IllegalArgumentException("Введите номер телефона в соответствие с примером");
        }
    }

    @Transactional(readOnly = true)
    public List<CarerRecord> findAllCarers(PetType petType) {
        if (petType == PetType.CAT) {
            List<CatCarer> catCarers = this.catCarerRepository.findAll();
            if (!catCarers.isEmpty()) {
                LOGGER.info("Was invoked method to find all cat carers");
                return catCarers.stream()
                        .map(this.modelMapper::mapToCarerRecord)
                        .toList();
            } else {
                LOGGER.info("Was invoked method to find all cat carers, but carers were not found");
                return new ArrayList<>();
            }
        } else if (petType == PetType.DOG) {
            List<DogCarer> dogCarers = this.dogCarerRepository.findAll();
            if (!dogCarers.isEmpty()) {
                LOGGER.info("Was invoked method to find all dog carers");
                return dogCarers.stream()
                        .map(this.modelMapper::mapToCarerRecord)
                        .toList();
            } else {
                LOGGER.info("Was invoked method to find all dog carers, but carers were not found");
                return new ArrayList<>();
            }
        } else {
            LOGGER.error("Wrong pet type");
            throw new IllegalArgumentException("Тип животного указан не верно");
        }
    }
}
