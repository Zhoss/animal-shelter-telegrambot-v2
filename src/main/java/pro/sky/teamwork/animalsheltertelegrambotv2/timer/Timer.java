//package pro.sky.teamwork.animalsheltertelegrambotv2.timer;
//
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
//import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
//import com.pengrad.telegrambot.model.request.ParseMode;
//import com.pengrad.telegrambot.request.SendMessage;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;
//import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogCarer;
//import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
//import pro.sky.teamwork.animalsheltertelegrambotv2.model.Command;
//import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
//import pro.sky.teamwork.animalsheltertelegrambotv2.service.AgreementService;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class DogTimer {
//    private final TelegramBot telegramBot;
//    private final VolunteerChatRepository volunteerChatRepository;
//    private final AgreementService dogAgreementService;
//
//    public DogTimer(TelegramBot telegramBot, VolunteerChatRepository volunteerChatRepository, AgreementService dogAgreementService) {
//        this.telegramBot = telegramBot;
//        this.volunteerChatRepository = volunteerChatRepository;
//        this.dogAgreementService = dogAgreementService;
//    }
//
//    @Scheduled(cron = "0 0 20 * * ?")
//    public void sendNotification() {
//        List<DogAgreement> dogAgreements = this.dogAgreementService.findAllAgreementsWithProbationByDate(LocalDate.now());
//        dogAgreements.forEach(agreement -> {
//            DogCarer dogCarer = agreement.getCarer();
//            DogDailyReport lastReport = dogCarer.getDailyReports().get(dogCarer.getDailyReports().size() - 1);
//
//            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(1))) {
//                sendPlainText(dogCarer.getChatId(), "Добрый день! Напоминаю, что " +
//                        "Вы не отправили отчет за прошлый день. Прошу прислать отчет!");
//            }
//            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(2))) {
//                sendPlainText(dogCarer.getChatId(), "Добрый день! Напоминаю, что " +
//                        "Вы не отправляли отчет больше двух дней. Прошу прислать отчет!");
//                SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(1L)
//                        .orElseThrow(() -> new RuntimeException("Чат волонтеров не найден"))
//                        .getTelegramChatId(),
//                        "Опекун " + dogCarer.getFullName() + " не отправлял отчет более двух дней." +
//                                "[User link](tg://user?id=" + dogCarer.getChatId() + " )");
//                sendMessageForVolunteer.parseMode(ParseMode.Markdown);
//                telegramBot.execute(sendMessageForVolunteer);
//            }
//        });
//    }
//
//    @Scheduled(cron = "0 0 10,16 * * ?")
//    public void notionForVolunteerThatProbationIsEnded() {
//        List<DogAgreement> dogAgreements = this.dogAgreementService.findAgreements().stream()
//                .filter(e -> e.getProbationEndData().minusDays(2).equals(LocalDate.now()))
//                .toList();
//        dogAgreements.forEach(agreement -> {
//            DogCarer dogCarer = agreement.getCarer();
//            SendMessage sendMessageForVolunteer = new SendMessage(
//                    volunteerChatRepository.findById(1L),
//                    "У опекуна " + dogCarer.getFullName() + ", телефон " + dogCarer.getPhoneNumber() +
//                            ", договор " + agreement.getNumber() + " от " + agreement.getConclusionDate() +
//                            " испытательный срок заканчивается через 2 дня. " +
//                            "Прошу принять решение о продлении испытательного срока");
//            List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(
//                    new InlineKeyboardButton("Продлить испытательный срок на 14 дней")
//                            .callbackData(Command.EXTEND_PROBATION_14_COMMAND.getCommand() + "/" + dogCarer.getId()),
//                    new InlineKeyboardButton("Продлить испытательный срок на 30 дней")
//                            .callbackData(Command.EXTEND_PROBATION_30_COMMAND.getCommand() + "/" + dogCarer.getId()),
//                    new InlineKeyboardButton("Испытательный срок пройден")
//                            .callbackData(Command.PROBATION_PASSED_COMMAND.getCommand() + "/" + dogCarer.getId()),
//                    new InlineKeyboardButton("Испытательный срок НЕ пройден")
//                            .callbackData(Command.PROBATION_NOT_PASSED_COMMAND.getCommand() + "/" + dogCarer.getId())
//            ));
//
//            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
//            buttons.forEach(keyboard::addRow);
//            sendMessageForVolunteer.replyMarkup(keyboard);
//            telegramBot.execute(sendMessageForVolunteer);
//        });
//    }
//
//    @Scheduled(cron = "0 0 21 * * ?")
//    public void sendNotificationForCheckDailyReports() {
//        SendMessage sendMessageForVolunteer = new SendMessage(this.volunteerChatRepository.findById(1L)
//                .orElseThrow(() -> new IllegalArgumentException("Чат волонтеров не найден"))
//                .getTelegramChatId(),
//                "Уважаемые волонтеры! Пожалуйста, просмотрите все присланные отчеты за сегодняшнее число. " +
//                        "При необходимости уведомить опекунов о более ответственном заполнение отчета, нажмите на кнопку.");
//        InlineKeyboardButton button = new InlineKeyboardButton("Уведомить опекунов")
//                .callbackData(Command.NOTIFY_CARERS_COMMAND.getCommand());
//
//        List<InlineKeyboardButton> buttons = new ArrayList<>(List.of(button));
//        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
//        buttons.forEach(keyboard::addRow);
//        sendMessageForVolunteer.replyMarkup(keyboard);
//        telegramBot.execute(sendMessageForVolunteer);
//    }
//
//    private void sendPlainText(long chatId, String text) {
//        telegramBot.execute(new SendMessage(chatId, text));
//    }
//}
