package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.VolunteerChat;

import java.util.Optional;

public interface VolunteerChatRepository extends JpaRepository<VolunteerChat, Long> {
    VolunteerChat findByTelegramChatId(long TelegramChatId);

    Optional<VolunteerChat> findByPetType(PetType petType);

    boolean existsByTelegramChatId(long TelegramChatId);
}
