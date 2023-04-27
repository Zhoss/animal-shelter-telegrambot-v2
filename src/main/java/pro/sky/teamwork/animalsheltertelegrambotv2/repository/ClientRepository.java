package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByTelegramChatId(long telegramChatId);

    boolean existsByTelegramChatId(long telegramChatId);
}
