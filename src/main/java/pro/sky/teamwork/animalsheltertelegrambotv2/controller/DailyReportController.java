package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.service.DailyReportService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class DailyReportController {
    private final DailyReportService dailyReportService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportController.class);

    public DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @Operation(summary = "Поиск ежедневных отчётов по ID опекуна",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденные ежедневные отчёты по ID опекуна",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(
                                                    implementation = DailyReport[].class))
                                    )
                            }
                    )
            },
            tags = "Отчет"
    )
    @GetMapping("/carer")
    public ResponseEntity<List<DailyReport>> findDailyReportsByCarerId(
            @Parameter(description = "ID опекуна",
                    example = "1") @RequestParam(name = "Идентификатор опекуна") Long carerId) {
        List<DailyReport> dailyReportByCarer = dailyReportService.findDailyReportByCarer(carerId);
        return ResponseEntity.ok(dailyReportByCarer);
    }

    @Operation(summary = "Поиск ежедневных отчётов по ID опекуна и дате отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденные ежедневные отчёты по ID опекуна и дате",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(
                                                    implementation = DailyReport[].class))
                                    )
                            }
                    )
            },
            tags = "Отчет"
    )
    @GetMapping("/carer-date")
    public ResponseEntity<DailyReport> findDailyReportsByCarerAndDate(
            @RequestParam Long carerId,
            @RequestParam LocalDate reportDate
    ) {
        LOGGER.info("Получение списка отчётов по опекуну и дате");
        DailyReport dailyReportByCarerAndDate =
                dailyReportService.findDailyReportByCarerAndDate(carerId, reportDate);
        return ResponseEntity.ok(dailyReportByCarerAndDate);
    }

    @GetMapping("/download-photo-by-date")
    public void downloadPhotoByByCarerIdAndDate(long carerId, LocalDate reportDate, HttpServletResponse response) {
        DailyReport dailyReport = this.dailyReportService.findDailyReportByCarerAndDate(carerId, reportDate);

        Path path = Path.of(dailyReport.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(dailyReport.getMediaType());
            response.setContentLength((int) dailyReport.getFileSize());
            is.transferTo(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
