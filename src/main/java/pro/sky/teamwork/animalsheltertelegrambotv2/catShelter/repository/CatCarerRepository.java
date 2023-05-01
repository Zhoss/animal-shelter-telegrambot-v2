package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;

import java.util.Optional;

public interface CatCarerRepository extends JpaRepository<CatCarer, Long> {
    Optional<CatCarer> findCatCarerByPhoneNumber(String phoneNumber);
    Optional<CatCarer> findCatCarerByChatId(long chatId);
}
