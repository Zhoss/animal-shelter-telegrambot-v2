package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class DailyReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportService.class);
    private final DailyReportRepository dailyReportRepository;
    private final ModelMapper modelMapper;

    public DailyReportService(DailyReportRepository dailyReportRepository, ModelMapper modelMapper) {
        this.dailyReportRepository = dailyReportRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод получения списка отчётов по опекуну.
     *
     * @param carerId
     * @return возвращает отчет по опекуну (по его id)
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportByCarerId(Long)
     */
    public List<DailyReportRecord> findDailyReportByCarer(Long carerId) {
        LOGGER.info("Получение списка отчётов по опекуну");
        List<DailyReport> dailyReports = dailyReportRepository.findDailyReportByCarerId(carerId);
        return dailyReports.stream()
                .map(this.modelMapper::mapToDailyRecordRecord)
                .collect(Collectors.toList());
    }

    /**
     * Метод получения списка отчётов по опекуну и дате отчёта.
     *
     * @param carerId
     * @return возвращает отчет по опекуну (по его id) и дате.
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportByCarerIdAndReportDate(Long, LocalDate)
     */
    public DailyReportRecord findDailyReportByCarerAndDate(Long carerId, LocalDate reportDate) {
        LOGGER.info("Получение списка отчётов по опекуну и дате отчёта");
        return this.modelMapper.mapToDailyRecordRecord(this.dailyReportRepository.findDailyReportByCarerIdAndReportDate(carerId, reportDate));
    }

    public DailyReport findDailyReportByCarerIdAndDate(Long carerId, LocalDate reportDate) {
        return this.dailyReportRepository.findDailyReportByCarerIdAndReportDate(carerId, reportDate);
    }

    public DailyReport addDailyReport(DailyReport dailyReport) {
        LOGGER.info("Был вызван метод по добавлению ежедневного отчета из TelegramBotUpdatesListener");
        return this.dailyReportRepository.save(dailyReport);
    }
}
