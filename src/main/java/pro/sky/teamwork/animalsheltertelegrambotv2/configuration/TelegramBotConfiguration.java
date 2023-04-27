package pro.sky.teamwork.animalsheltertelegrambotv2.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Command;

@Configuration
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        bot.execute(new SetMyCommands(
                new BotCommand(Command.START_COMMAND.getCommand(),
                        Command.START_COMMAND.getDescription()),
                new BotCommand(Command.SHELTER_INFO_COMMAND.getCommand(),
                        Command.SHELTER_INFO_COMMAND.getDescription()),
                new BotCommand(Command.SHELTER_MAIN_INFO_COMMAND.getCommand(),
                        Command.SHELTER_MAIN_INFO_COMMAND.getDescription()),
                new BotCommand(Command.SHELTER_WORK_SCHEDULE_COMMAND.getCommand(),
                        Command.SHELTER_WORK_SCHEDULE_COMMAND.getDescription()),
                new BotCommand(Command.SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.getCommand(),
                        Command.SHELTER_SAFETY_RECOMMENDATIONS_COMMAND.getDescription()),
                new BotCommand(Command.WRITE_CLIENT_CONTACT_COMMAND.getCommand(),
                        Command.WRITE_CLIENT_CONTACT_COMMAND.getDescription()),
                new BotCommand(Command.CALL_VOLUNTEER_COMMAND.getCommand(),
                        Command.CALL_VOLUNTEER_COMMAND.getDescription()),
                new BotCommand(Command.BACK_COMMAND.getCommand(),
                        Command.BACK_COMMAND.getDescription()),
                new BotCommand(Command.TAKE_A_PET_COMMAND.getCommand(),
                        Command.TAKE_A_PET_COMMAND.getDescription()),
                new BotCommand(Command.INTRODUCTION_TO_PET_COMMAND.getCommand(),
                        Command.INTRODUCTION_TO_PET_COMMAND.getDescription()),
                new BotCommand(Command.TAKE_DOCUMENTS_LIST_COMMAND.getCommand(),
                        Command.TAKE_DOCUMENTS_LIST_COMMAND.getDescription()),
                new BotCommand(Command.TRANSFER_A_PET_COMMAND.getCommand(),
                        Command.TRANSFER_A_PET_COMMAND.getDescription()),
                new BotCommand(Command.ENVIRONMENT_FOR_YOUNG_PET_COMMAND.getCommand(),
                        Command.ENVIRONMENT_FOR_YOUNG_PET_COMMAND.getDescription()),
                new BotCommand(Command.ENVIRONMENT_FOR_PET_COMMAND.getCommand(),
                        Command.ENVIRONMENT_FOR_PET_COMMAND.getDescription()),
                new BotCommand(Command.ENVIRONMENT_FOR_LIMITED_PET_COMMAND.getCommand(),
                        Command.ENVIRONMENT_FOR_LIMITED_PET_COMMAND.getDescription()),
                new BotCommand(Command.USUAL_REFUSALS_COMMAND.getCommand(),
                        Command.USUAL_REFUSALS_COMMAND.getDescription()),
                new BotCommand(Command.SEND_REPORT_MENU_COMMAND.getCommand(),
                        Command.SEND_REPORT_MENU_COMMAND.getDescription()),
                new BotCommand(Command.SEND_REPORT_COMMAND.getCommand(),
                        Command.SEND_REPORT_COMMAND.getDescription())
        ));
        return bot;
    }
}
