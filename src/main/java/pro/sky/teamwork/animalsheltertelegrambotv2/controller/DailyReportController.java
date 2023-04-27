package pro.sky.teamwork.animalsheltertelegrambotv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pro.sky.teamwork.animalsheltertelegrambotv2.dto.DailyReportRecord;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;
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

    public DailyReportController(DailyReportService dailyReportService) {
        this.dailyReportService = dailyReportService;
    }

    @Operation(
            summary = "Поиск всех ежедневных отчётов опекуна по ID опекуна",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденные ежедневные отчёты по ID опекуна",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(
                                                    implementation = DogDailyReport[].class))
                                    )
                            }
                    )
            },
            tags = "Ежедневный отчет"
    )
    @GetMapping("/carer")
    public ResponseEntity<List<DailyReportRecord>> findDailyReportsByCarerId(
            @Parameter(description = "ID опекуна", example = "1")
            @RequestParam(name = "Идентификатор опекуна") Long carerId,
            @RequestParam PetType petType) {
        List<DailyReportRecord> dailyReportByCarer = dailyReportService
                .findDailyReportsByCarer(carerId, petType);
        return ResponseEntity.ok(dailyReportByCarer);
    }

    @Operation(
            summary = "Поиск ежедневных отчётов по ID опекуна и дате отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Найденные ежедневные отчёты по ID опекуна и дате",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(
                                                    implementation = DogDailyReport[].class))
                                    )
                            }
                    )
            },
            tags = "Ежедневный отчет"
    )
    @GetMapping("/carer-date")
    public ResponseEntity<DailyReportRecord> findDailyReportsByCarerAndDate(
            @RequestParam Long carerId,
            @RequestParam LocalDate reportDate,
            @RequestParam PetType petType) {
        DailyReportRecord dailyReportByCarerAndDate = dailyReportService
                .findDailyReportByCarerAndDate(carerId, reportDate, petType);
        return ResponseEntity.ok(dailyReportByCarerAndDate);
    }

    @Operation(
            summary = "Поиск ежедневных отчётов по дате отчета",
            tags = "Ежедневный отчет"
    )
    @GetMapping("/date")
    public ResponseEntity<List<DailyReportRecord>> findDailyReportsByDate(
            @Parameter(description = "Дата отчета", example = "2023-01-01")
            @RequestParam(name = "Дата отчета") LocalDate localDate,
            @RequestParam PetType petType) {
        List<DailyReportRecord> dailyReportsByDate = dailyReportService
                .findDailyReportsByDate(localDate, petType);
        return ResponseEntity.ok(dailyReportsByDate);
    }

    @Operation(
            summary = "Загрузка фотографии из отчета, найденного по ID опекуна и дате",
            tags = "Ежедневный отчет"
    )
    @GetMapping("/download-photo-by-date")
    public void downloadPhotoByByCarerIdAndDate(@RequestParam long carerId,
                                                @RequestParam LocalDate reportDate,
                                                @RequestParam PetType petType,
                                                HttpServletResponse response) {
        DailyReport dailyReport = this.dailyReportService
                .findDailyReportByCarerIdAndDate(carerId, reportDate, petType);

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

    @Operation(
            summary = "Удаление отчета по ID отчета",
            tags = "Ежедневный отчет"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDailyReport(@PathVariable long id,
                                               @RequestParam PetType petType) {
        this.dailyReportService.deleteDailyReport(id, petType);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
