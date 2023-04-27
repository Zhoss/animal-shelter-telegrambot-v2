package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;

import java.util.Optional;

public interface DogCarerRepository extends JpaRepository<DogCarer, Long> {
    Optional<DogCarer> findDogCarerByPhoneNumber(String phoneNumber);

    Optional<DogCarer> findDogCarerByChatId(long chatId);
}
