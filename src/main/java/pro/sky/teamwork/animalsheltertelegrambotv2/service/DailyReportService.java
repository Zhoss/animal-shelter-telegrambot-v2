package pro.sky.teamwork.animalsheltertelegrambotv2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.repository.DailyReportRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
public class DailyReportService {
    private final DailyReportRepository dailyReportRepository;

    private static final Logger logger = LoggerFactory.getLogger(DailyReportService.class);

    public DailyReportService (DailyReportRepository dailyReportRepository) {
        this.dailyReportRepository = dailyReportRepository;
    }

    public List<DailyReport> findDailyReportByCarer(Long carerId) {
        logger.info("Получение списка отчётов по опекуну");
        return dailyReportRepository.findDailyReportByCarerId(carerId);
    }

    public List<DailyReport> findDailyReportByCarerAndDate(Long carerId, LocalDate reportDate) {
        logger.info("Получение списка отчётов по опекуну и дате отчёта");
        return dailyReportRepository.findDailyReportByCarerIdAndReportDate(carerId, reportDate);
    }

}