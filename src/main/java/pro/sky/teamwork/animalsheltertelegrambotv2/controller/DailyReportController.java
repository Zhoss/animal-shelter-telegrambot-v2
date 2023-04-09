package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/carer/reports")
public class DailyReportController {

    private final DailyReportService dailyReportService;

    private static final Logger logger = LoggerFactory.getLogger(DailyReportController.class);

    public DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @GetMapping("/carer")
    public ResponseEntity<List<DailyReport>> findDailyReportByCarerId(@RequestParam Long carerId) {
        var dailyReportByCarer = dailyReportService.findDailyReportByCarer(carerId);
        return ResponseEntity.ok(dailyReportByCarer);
    }

    @GetMapping("/carerdate")
    public ResponseEntity<List<DailyReport>> findDailyReportByCarerAndDate(
            @RequestParam Long carerId,
            @RequestParam LocalDate reportDate
            ) {
        logger.info("Получение списка отчётов по опекуну и дате");
        var dailyReportByCarerAndDate =
                dailyReportService.findDailyReportByCarerAndDate(carerId, reportDate);
        return ResponseEntity.ok(dailyReportByCarerAndDate);
    }
}
