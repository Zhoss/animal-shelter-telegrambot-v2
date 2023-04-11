package pro.sky.teamwork.animalsheltertelegrambotv2.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.sky.teamwork.animalsheltertelegrambotv2.listener.TelegramBotUpdatesListener;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        bot.execute(new SetMyCommands(
                TelegramBotUpdatesListener.START_COMMAND,
                TelegramBotUpdatesListener.SHELTER_INFO_COMMAND,
                TelegramBotUpdatesListener.SHELTER_MAIN_INFO_COMMAND,
                TelegramBotUpdatesListener.SHELTER_WORK_SCHEDULE_COMMAND,
                TelegramBotUpdatesListener.SHELTER_SAFETY_RECOMMENDATIONS_COMMAND,
                TelegramBotUpdatesListener.WRITE_CLIENT_CONTACT_COMMAND,
                TelegramBotUpdatesListener.CALL_VOLUNTEER_COMMAND,
                TelegramBotUpdatesListener.BACK_COMMAND,
                TelegramBotUpdatesListener.TAKE_A_DOG_COMMAND,
                TelegramBotUpdatesListener.INTRODUCTION_TO_DOG_COMMAND,
                TelegramBotUpdatesListener.TAKE_DOCUMENTS_LIST_COMMAND,
                TelegramBotUpdatesListener.TRANSFER_A_DOG_COMMAND,
                TelegramBotUpdatesListener.ENVIRONMENT_FOR_PUPPY_COMMAND,
                TelegramBotUpdatesListener.ENVIRONMENT_FOR_DOG_COMMAND,
                TelegramBotUpdatesListener.ENVIRONMENT_FOR_LIMITED_DOG_COMMAND,
                TelegramBotUpdatesListener.CYNOLOGIST_ADVICES_COMMAND,
                TelegramBotUpdatesListener.CYNOLOGIST_CONTACTS_COMMAND,
                TelegramBotUpdatesListener.USUAL_REFUSALS_COMMAND,
                TelegramBotUpdatesListener.SEND_REPORT_COMMAND
        ));
        return bot;
    }
}
