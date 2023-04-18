//package pro.sky.teamwork.animalsheltertelegrambotv2.timer;
//
//import com.pengrad.telegrambot.TelegramBot;
//import com.pengrad.telegrambot.request.SendMessage;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
//import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
//import pro.sky.teamwork.animalsheltertelegrambotv2.repository.CarerRepository;
//import pro.sky.teamwork.animalsheltertelegrambotv2.repository.VolunteerChatRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@EnableScheduling
//@Component
//public class Timer {
//    private final TelegramBot telegramBot;
//    private final VolunteerChatRepository volunteerChatRepository;
//    private final CarerRepository carerRepository;
//
//    public Timer(TelegramBot telegramBot, VolunteerChatRepository volunteerChatRepository, CarerRepository carerRepository) {
//        this.telegramBot = telegramBot;
//        this.volunteerChatRepository = volunteerChatRepository;
//        this.carerRepository = carerRepository;
//    }
//
//    @Scheduled(cron = "0 0 14 * * ?") // Запуск напоминания ежедневно в 14:00
//    public void sendDailyReminder() {
//        List<Carer> carers = this.carerRepository.findAll();
//        carers.forEach(carer -> {
//            List<DailyReport> dailyReports = carer.getDailyReports();
//            int dailyReportSize = dailyReports.size();
//            DailyReport lastReport = dailyReports.get(dailyReportSize - 1);
//
//            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(1))) {
//                sendMessage(carer.getChatId(), "Вы не отправили отчет за прошлый день.");
//            }
//            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(2))) {
//                sendMessage(carer.getChatId(), "Вы не отправляли отчет больше двух дней");
//                sendMessage(this.volunteerChatRepository.findById(1L)
//                                .orElseThrow(() -> new RuntimeException("Чат волонтеров не найден"))
//                                .getTelegramChatId(),
//                        String.format("Опекун %s не отправлял отчет более\n" +
//                                " двух дней", carer.getFullName()));
//            }
//        });
//    }
//
//    private void sendMessage(long chatId, String text) {
//        telegramBot.execute(new SendMessage(chatId, text));
//    }
////}
