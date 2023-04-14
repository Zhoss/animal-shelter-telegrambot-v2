package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;

public interface CarerRepository extends JpaRepository<Carer, Long> {
    boolean existsCarerByFullNameAndPhoneNumber(String fullName, String phoneNumber);

    Carer findCarerByPhoneNumber(String phoneNumber);

    Carer findCarerByChatId(long chatId);
}
