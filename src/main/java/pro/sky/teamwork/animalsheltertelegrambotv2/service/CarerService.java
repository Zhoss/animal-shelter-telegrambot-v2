package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.exceprion.CarerNotFoundException;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.CarerRepository;

@Service
public class CarerService {
    private final CarerRepository carerRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(CarerService.class);

    public CarerService(CarerRepository carerRepository) {
        this.carerRepository = carerRepository;
    }

    /**
     *Добавление данных об опекуне животного
     *
     * @param fullName <b>ФИО</b>
     * @param phoneNumber <b>Телефонный номер</b>
     * <br>//@param setAge <b>Возраст</b>
     * @see CarerRepository
     */
    @Transactional
    public Carer addCarer(String fullName, int age, String phoneNumber){
        if (!fullName.isEmpty() && !fullName.isBlank() &&
                !phoneNumber.isEmpty() && !phoneNumber.isBlank()) {
            Carer carer = new Carer();
            carer.setFullName(fullName);
            carer.setAge(age);
            carer.setPhoneNumber(phoneNumber);
            carerRepository.save(carer);
            return carer;
        } else {
            LOGGER.error("Carer's full name or phone number is empty");
            throw new IllegalArgumentException("Требуется указать корректные данные: имя опекуна, телефонный номер опекуна");
        }
    }

    public Carer findCarer(long id) {
        if (id < 0) {
            LOGGER.error("Input id = " + id + " for getting carer is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        }
        return this.carerRepository.findById(id).
                orElseThrow(() -> new CarerNotFoundException("Опекун с id = " + id + " не найден"));
    }
}
