package pro.sky.teamwork.animalsheltertelegrambotv2.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Timer {
    private final TelegramBot telegramBot;
    private final VolunteerChatRepository volunteerChatRepository;
    private final AgreementService agreementService;

    public Timer(TelegramBot telegramBot, VolunteerChatRepository volunteerChatRepository, AgreementService agreementService) {
        this.telegramBot = telegramBot;
        this.volunteerChatRepository = volunteerChatRepository;
        this.agreementService = agreementService;
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void sendNotification() {
        List<Agreement> agreements = this.agreementService.findAllAgreementsWithProbationByDate(LocalDate.now());
        agreements.forEach(agreement -> {
            Carer carer = agreement.getCarer();
            DailyReport lastReport = carer.getDailyReports().get(carer.getDailyReports().size() - 1);

            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(1))) {
                sendPlainText(carer.getChatId(), "Добрый день! Напоминаю, что " +
                        "Вы не отправили отчет за прошлый день. Прошу прислать отчет!");
            }
            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(2))) {
                sendPlainText(carer.getChatId(), "Добрый день! Напоминаю, что " +
                        "Вы не отправляли отчет больше двух дней. Прошу прислать отчет!");
                SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Чат волонтеров не найден"))
                        .getTelegramChatId(),
                        "Опекун " + carer.getFullName() + " не отправлял отчет более двух дней." +
                                "[User link](tg://user?id=" + carer.getChatId() + " )");
                sendMessageForVolunteer.parseMode(ParseMode.Markdown);
                telegramBot.execute(sendMessageForVolunteer);
            }
        });
    }

    @Scheduled(cron = "0 0 10,16 * * ?")
    public void notionForVolunteerThatProbationIsEnded() {
        List<Agreement> agreements = this.agreementService.findAgreements().stream()
                .filter(e -> e.getProbationEndData().minusDays(2).equals(LocalDate.now()))
                .toList();
        agreements.forEach(agreement -> {
            Carer carer = agreement.getCarer();
            SendMessage sendMessageForVolunteer = new SendMessage(
                    volunteerChatRepository.findById(1L),
                    "У опекуна " + carer.getFullName() + ", телефон " + carer.getPhoneNumber() +
                            ", договор " + agreement.getNumber() + " от " + agreement.getConclusionDate() +
                            " испытательный срок заканчивается через 2 дня. " +
                            "Прошу принять решение о продлении испытательного срока");
            List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                    new InlineKeyboardButton("Продлить испытательный срок на 14 дней")
                            .callbackData(Command.EXTEND_PROBATION_14_COMMAND.getCommand() + "/" + carer.getId()),
                    new InlineKeyboardButton("Продлить испытательный срок на 30 дней")
                            .callbackData(Command.EXTEND_PROBATION_30_COMMAND.getCommand() + "/" + carer.getId()),
                    new InlineKeyboardButton("Испытательный срок пройден")
                            .callbackData(Command.PROBATION_PASSED_COMMAND.getCommand() + "/" + carer.getId()),
                    new InlineKeyboardButton("Испытательный срок НЕ пройден")
                            .callbackData(Command.PROBATION_NOT_PASSED_COMMAND.getCommand() + "/" + carer.getId())
            ));

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            buttons.forEach(keyboard::addRow);
            sendMessageForVolunteer.replyMarkup(keyboard);
            telegramBot.execute(sendMessageForVolunteer);
        });
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void sendNotificationForCheckDailyReports() {
        SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Чат волонтеров не найден"))
                .getTelegramChatId(),
                "Уважаемые волонтеры! Пожалуйста, просмотрите все присланные отчеты за сегодняшнее число. " +
                        "При необходимости уведомить опекунов о более ответственном заполнение отчета, нажмите на кнопку.");
        InlineKeyboardButton button = new InlineKeyboardButton("Уведомить опекунов")
                .callbackData(Command.NOTIFY_CARERS_COMMAND.getCommand());

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(button));
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);
        sendMessageForVolunteer.replyMarkup(keyboard);
        telegramBot.execute(sendMessageForVolunteer);
    }

    private void sendPlainText(long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
