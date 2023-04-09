package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {
    List<DailyReport> findDailyReportByCarerId(Long carerId);

    List<DailyReport> findDailyReportByCarerIdAndReportDate(Long carerId, LocalDate reportDate);

    List<DailyReport> findDailyReportsByDogId(Integer dogId);

    DailyReport findDailyReportsByDogIdAndReportDateIs(Integer dogId,LocalDate date);


}
