package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;

import java.util.Optional;

public interface CarerRepository extends JpaRepository<Carer, Long> {
    Optional<Carer> findCarerByPhoneNumber(String phoneNumber);

    Optional<Carer> findCarerByChatId(long chatId);

    Carer findCarerByDogId(long dog);
}
