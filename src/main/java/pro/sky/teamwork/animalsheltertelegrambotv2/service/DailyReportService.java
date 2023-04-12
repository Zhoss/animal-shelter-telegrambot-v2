package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository;

import java.time.LocalDate;
import java.util.List;


@Service
public class DailyReportService {
    private final DailyReportRepository dailyReportRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportService.class);

    public DailyReportService(DailyReportRepository dailyReportRepository) {
        this.dailyReportRepository = dailyReportRepository;
    }

    /**
     * Метод получения списка отчётов по опекуну.
     *
     * @param carerId
     * @return возвращает отчет по опекуну (по его id)
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportByCarerId(Long)
     */
    public List<DailyReport> findDailyReportByCarer(Long carerId) {
        LOGGER.info("Получение списка отчётов по опекуну");
        return dailyReportRepository.findDailyReportByCarerId(carerId);
    }

    /**
     * Метод получения списка отчётов по опекуну и дате отчёта.
     *
     * @param carerId    идентификатор опекуна
     * @param reportDate дата отчета
     * @return возвращает отчет по опекуну (по его id) и дате.
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportByCarerIdAndReportDate(Long, LocalDate)
     */
    public List<DailyReport> findDailyReportByCarerAndDate(Long carerId, LocalDate reportDate) {
        LOGGER.info("Получение списка отчётов по опекуну и дате отчёта");
        return dailyReportRepository.findDailyReportByCarerIdAndReportDate(carerId, reportDate);
    }

    /**
     * Метод получения списка отчётов по опекуну и дате отчёта.
     *
     * @param dogId идентификатор собаки
     * @return возвращает отчет по собаке (по ее id).
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportsByDogId
     */
    public List<DailyReport> findDailyReportsByDogId(Integer dogId) {
        LOGGER.info("Получение списка отчётов по ID собаки");
        return dailyReportRepository.findDailyReportsByDogId(dogId);
    }

    /**
     * Метод получения списка отчётов по опекуну и дате отчёта.
     *
     * @param dogId идентификатор собаки
     * @param date  дата получения отчета
     * @return возвращает отчет по собаке (по ее id) и дате отчета.
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportsByDogIdAndReportDateIs
     */
    public DailyReport findDailyReportByDogAndReportDate(Integer dogId, LocalDate date) {
        LOGGER.info("Получение отчёта по ID собаки на определённую дату");
        return dailyReportRepository.findDailyReportsByDogIdAndReportDateIs(dogId, date);
    }
}
