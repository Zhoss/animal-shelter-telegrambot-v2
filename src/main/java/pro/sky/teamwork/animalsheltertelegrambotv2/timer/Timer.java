package pro.sky.teamwork.animalsheltertelegrambotv2.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Command;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Pet;
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
        List<CatAgreement> catAgreements = this.agreementService.findAllAgreementsWithProbationByDate(LocalDate.now(), Pet.CAT);
        List<DogAgreement> dogAgreements = this.agreementService.findAllAgreementsWithProbationByDate(LocalDate.now(), Pet.DOG);
        catAgreements.forEach(catAgreement -> {
            CatCarer catCarer = catAgreement.getCarer();
            CatDailyReport lastReport = catCarer.getDailyReports().get(catCarer.getDailyReports().size() - 1);

            sendNotification(lastReport.getReportDate(), catCarer.getChatId(), catCarer.getFullName(), 1L);
        });
        dogAgreements.forEach(dogAgreement -> {
            DogCarer dogCarer = dogAgreement.getCarer();
            DogDailyReport lastReport = dogCarer.getDailyReports().get(dogCarer.getDailyReports().size() - 1);

            sendNotification(lastReport.getReportDate(), dogCarer.getChatId(), dogCarer.getFullName(), 2L);
        });
    }

    private void sendNotification(LocalDate localDate, long chatId, String fullName, long volunteerChatId) {
        if (localDate.isBefore(LocalDate.now().minusDays(1))) {
            sendPlainText(chatId, "Добрый день! Напоминаю, что " +
                    "Вы не отправили отчет за прошлый день. Прошу прислать отчет!");
        }
        if (localDate.isBefore(LocalDate.now().minusDays(2))) {
            sendPlainText(chatId, "Добрый день! Напоминаю, что " +
                    "Вы не отправляли отчет больше двух дней. Прошу прислать отчет!");
            SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(volunteerChatId)
                    .orElseThrow(() -> new RuntimeException("Чат волонтеров не найден"))
                    .getTelegramChatId(),
                    "Опекун " + fullName + " не отправлял отчет более двух дней." +
                            "[Информация об опекуне](tg://user?id=" + chatId + " )");
            sendMessageForVolunteer.parseMode(ParseMode.Markdown);
            telegramBot.execute(sendMessageForVolunteer);
        }
    }

    @Scheduled(cron = "0 0 10,16 * * ?")
    public void notionForVolunteerThatProbationIsEnded() {
        List<CatAgreement> catAgreements = this.agreementService.findAgreementsWithEndingProbation(Pet.CAT);
        List<DogAgreement> dogAgreements = this.agreementService.findAgreementsWithEndingProbation(Pet.DOG);
        catAgreements.forEach(catAgreement -> {
            CatCarer catCarer = catAgreement.getCarer();
            sendEndProbationNotification(catCarer.getFullName(),
                    catCarer.getPhoneNumber(),
                    catAgreement.getNumber(),
                    catAgreement.getConclusionDate(),
                    1L,
                    catCarer.getId());
        });
        dogAgreements.forEach(dogAgreement -> {
            DogCarer dogCarer = dogAgreement.getCarer();
            sendEndProbationNotification(dogCarer.getFullName(),
                    dogCarer.getPhoneNumber(),
                    dogAgreement.getNumber(),
                    dogAgreement.getConclusionDate(),
                    2L,
                    dogCarer.getId());
        });
    }

    private void sendEndProbationNotification(String fullName,
                                              String phoneNumber,
                                              String agreementNumber,
                                              LocalDate agreementConclusionDate,
                                              long volunteerChatId,
                                              long carerId) {
        SendMessage sendMessageForVolunteer = new SendMessage(
                this.volunteerChatRepository.findById(volunteerChatId)
                        .orElseThrow(() -> new IllegalArgumentException("Чат волонтеров не найден"))
                        .getTelegramChatId(),
                "У опекуна " + fullName + ", телефон " + phoneNumber +
                        ", договор " + agreementNumber + " от " + agreementConclusionDate +
                        " испытательный срок заканчивается через 2 дня. " +
                        "Прошу принять решение о продлении испытательного срока");

        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
                new InlineKeyboardButton("Продлить испытательный срок на 14 дней")
                        .callbackData(Command.EXTEND_PROBATION_14_COMMAND.getCommand() + "/" + carerId),
                new InlineKeyboardButton("Продлить испытательный срок на 30 дней")
                        .callbackData(Command.EXTEND_PROBATION_30_COMMAND.getCommand() + "/" + carerId),
                new InlineKeyboardButton("Испытательный срок пройден")
                        .callbackData(Command.PROBATION_PASSED_COMMAND.getCommand() + "/" + carerId),
                new InlineKeyboardButton("Испытательный срок НЕ пройден")
                        .callbackData(Command.PROBATION_NOT_PASSED_COMMAND.getCommand() + "/" + carerId)
        ));

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        buttons.forEach(keyboard::addRow);
        sendMessageForVolunteer.replyMarkup(keyboard);
        telegramBot.execute(sendMessageForVolunteer);
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void sendNotificationForCheckDailyReports() {
        for (long i = 1; i < 3; i++) {
            SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(i)
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
    }

    private void sendPlainText(long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
