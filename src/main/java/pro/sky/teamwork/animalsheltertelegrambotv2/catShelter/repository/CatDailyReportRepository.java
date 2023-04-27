package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatDailyReport;

import java.time.LocalDate;
import java.util.List;

public interface CatDailyReportRepository extends JpaRepository<CatDailyReport, Long> {
    List<CatDailyReport> findCatDailyReportByCatCarerId(Long carerId);

    CatDailyReport findCatDailyReportByCatCarerIdAndReportDate(Long carerId, LocalDate reportDate);

    List<CatDailyReport> findCatDailyReportsByReportDate(LocalDate localDate);
}
