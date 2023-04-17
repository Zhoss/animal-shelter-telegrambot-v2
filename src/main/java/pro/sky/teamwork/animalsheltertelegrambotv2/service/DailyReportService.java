package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @Transactional(readOnly = true)
    public List<DailyReportRecord> findDailyReportsByCarer(Long carerId) {
        if (carerId > 0) {
            List<DailyReport> dailyReports = dailyReportRepository.findDailyReportByCarerId(carerId);
            if (!dailyReports.isEmpty()) {
                LOGGER.info("Was invoked method to find daily reports by carer id");
                return dailyReports.stream()
                        .map(this.modelMapper::mapToDailyRecordRecord)
                        .collect(Collectors.toList());
            } else {
                LOGGER.info("Was invoked method to find all daily reports by carer id, but daily reports were not found");
                return new ArrayList<>();
            }
        } else {
            LOGGER.error("Input carer id = " + carerId + " to find daily reports is incorrect");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна");
        }
    }

    /**
     * Метод получения списка отчётов по опекуну и дате отчёта.
     *
     * @param carerId
     * @return возвращает отчет по опекуну (по его id) и дате.
     * @see pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository#findDailyReportByCarerIdAndReportDate(Long, LocalDate)
     */
    @Transactional(readOnly = true)
    public DailyReportRecord findDailyReportByCarerAndDate(Long carerId, LocalDate reportDate) {
        if (carerId > 0 && reportDate != null) {
            LOGGER.info("Was invoked method to find daily report by carer id and the specified date = " + reportDate);
            return this.modelMapper.mapToDailyRecordRecord(this.dailyReportRepository.findDailyReportByCarerIdAndReportDate(carerId, reportDate));
        } else {
            LOGGER.error("Input carer id = " + carerId + " is incorrect and/or input object 'reportDate' is null");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна и/или корректную дату");
        }
    }

    @Transactional(readOnly = true)
    public DailyReport findDailyReportByCarerIdAndDate(Long carerId, LocalDate reportDate) {
        if (carerId > 0 && reportDate != null) {
            LOGGER.info("Was invoked method to find daily report by carer id and the specified date = " +
                    reportDate + " from Telegram bot");
            return this.dailyReportRepository.findDailyReportByCarerIdAndReportDate(carerId, reportDate);
        } else {
            LOGGER.error("Input carer id = " + carerId + " is incorrect and/or input object 'reportDate' is null");
            throw new IllegalArgumentException("Требуется указать корректный id опекуна и/или корректную дату");
        }
    }

    public void addDailyReport(DailyReport dailyReport) {
        if (dailyReport != null) {
            LOGGER.info("Was invoked method for adding daily report from Telegram bot");
            this.dailyReportRepository.save(dailyReport);
        } else {
            LOGGER.error("Input object 'dailyReport' is null");
            throw new IllegalArgumentException("Input object 'dailyReport' is null");
        }
    }
}
