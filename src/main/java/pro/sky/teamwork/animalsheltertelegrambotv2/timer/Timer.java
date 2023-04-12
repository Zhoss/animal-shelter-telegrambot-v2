package pro.sky.teamwork.animalsheltertelegrambotv2.timer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.CarerService;

import java.time.LocalDate;
import java.util.List;

@EnableScheduling
@Component
public class Timer {
    private final CarerService carerService;
    private final TelegramBot telegramBot;

    private long supportId;

    public Timer(CarerService carerService, TelegramBot telegramBot) {
        this.carerService = carerService;
        this.telegramBot = telegramBot;
    }

    @Value("${telegram.bot.support.chat}") //сюда подставить Id волонтера
    public void setSupportId(long supportId) {
        this.supportId = supportId;
    }

    /**
     * Метод отправки напоминания опекуну о том, что необходимо прислать отчет.
     */
    @Scheduled(cron = "0 0 14 * * ?") // Запуск напоминания ежедневно в 14:00
    public void sendDailyReminder() {
        List<Carer> carers = carerService.findAll();
        carers.forEach(carer -> {
            List<DailyReport> dailyReports = carer.getDailyReports();
            var dailyReportSize = dailyReports.size();
            DailyReport lastReport = dailyReports.get(dailyReportSize - 1);

            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(1))) {
                sendMessage(carer.getChatId(), "Вы не отправили отчет за прошлый день.");
            }
            if (lastReport.getReportDate().isBefore(LocalDate.now().minusDays(2))) {
//                sendMessage(carer.getChatId(), "Вы не отправляли отчет больше двух дней");
                sendMessage(supportId, String.format("Опекун %s не отправлял отчет более\n" +
                                " двух дней",carer.getFullName()));
            }
        });
    }

    private BaseResponse sendMessage(long chatId, String text) {
        SendMessage request = new SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        return telegramBot.execute(request);
    }
}
