package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.VolunteerChat;

public interface VolunteerChatRepository extends JpaRepository<VolunteerChat, Long> {
    VolunteerChat findByTelegramChatId(long TelegramChatId);

    boolean existsByTelegramChatId(long TelegramChatId);
}
