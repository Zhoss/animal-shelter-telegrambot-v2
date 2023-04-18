package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.CarerRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.exception.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.CarerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CarerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CarerService.class);
    private final CarerRepository carerRepository;
    private final ModelMapper modelMapper;

    public CarerService(CarerRepository carerRepository, ModelMapper modelMapper) {
        this.carerRepository = carerRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Добавление информации по опекуну через телеграм бота.
     *
     * @param fullName    {@link Carer#setFullName(String)}
     * @param age         {@link Carer#setBirthYear(int)} - преобразовывает полученную дату рождения в возраст.
     * @param phoneNumber {@link Carer#setPhoneNumber(String)}
     * @return данные по опекуну добавлены
     * @throws IllegalArgumentException Если же поля данных: Имя и телефон пустые
     * @see CarerRepository
     */
    @Transactional
    public Carer addCarer(String fullName, int age, String phoneNumber, long chatId) {
        if (!fullName.isEmpty() && !fullName.isBlank() &&
                !phoneNumber.isEmpty() && !phoneNumber.isBlank()) {
            Carer carer = new Carer();
            carer.setFullName(fullName);
            carer.setBirthYear(LocalDate.now().getYear() - age);
            carer.setPhoneNumber(phoneNumber);
            carer.setChatId(chatId);
            LOGGER.info("Was invoked method for adding carer from Telegram bot");
            this.carerRepository.save(carer);
            return carer;
        } else {
            LOGGER.error("Carer's full name or phone number is empty");
            throw new IllegalArgumentException("Требуется указать корректные данные: имя опекуна, телефонный номер опекуна");
        }
    }

    @Transactional
    public Carer saveCarer(Carer carer) {
        if (carer != null) {
            LOGGER.info("Was invoked method for saving carer from Telegram bot");
            return this.carerRepository.save(carer);
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
    public CarerRecord findCarer(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for getting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        }
        LOGGER.info("Was invoked method to find carer");
        Carer carer = this.carerRepository.findById(id).
                orElseThrow(() -> new CarerNotFoundException("Опекун с id = " + id + " не найден"));
        return this.modelMapper.mapToCarerRecord(carer);
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
    public Carer findCarerByChatId(long chatId) {
        if (chatId != 0) {
            LOGGER.info("Was invoked method to find carer by chat id");
            return this.carerRepository.findCarerByChatId(chatId)
                    .orElse(null);
        } else {
            LOGGER.error("Input chat id = " + chatId + " for getting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id чата опекуна");
        }
    }

    @Transactional
    public CarerRecord editCarer(CarerRecord carerRecord) {
        if (carerRecord != null) {
            LOGGER.info("Was invoked method to edit carer");
            Carer carer = this.carerRepository.findById(carerRecord.getId())
                    .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
            this.modelMapper.updateCarer(carerRecord, carer);
            this.carerRepository.save(carer);
            return this.modelMapper.mapToCarerRecord(carer);
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
    public void deleteCarer(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for deleting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        } else {
            LOGGER.info("Was invoked method to delete carer");
            this.carerRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public CarerRecord findCarerByPhoneNumber(String phoneNumber) {
        LOGGER.info("Getting Carer by his phone number");
        Pattern pattern = Pattern.compile("^(\\+\\d{1,7}\\(\\d{3}\\)\\d{7})$");
        Matcher matcher = pattern.matcher(phoneNumber);
        if (matcher.matches()) {
            Carer carer = this.carerRepository.findCarerByPhoneNumber(phoneNumber)
                    .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
            return this.modelMapper.mapToCarerRecord(carer);
        } else {
            LOGGER.error("Input phone number = " + phoneNumber + " is incorrect");
            throw new IllegalArgumentException("Введите номер телефона в соответствие с примером");
        }
    }

    @Transactional(readOnly = true)
    public List<CarerRecord> findAllCarers() {
        List<Carer> carerRecords = this.carerRepository.findAll();
        if (!carerRecords.isEmpty()) {
            LOGGER.info("Was invoked method to find all carers");
            return carerRecords.stream()
                    .map(this.modelMapper::mapToCarerRecord)
                    .toList();
        } else {
            LOGGER.info("Was invoked method to find all carers, but carers were not found");
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Carer findCarerByDogId(long dogId) {
        return carerRepository.findCarerByDogId(dogId)
                .orElseThrow(() -> new CarerNotFoundException("Опекун не найден"));
    }
}
