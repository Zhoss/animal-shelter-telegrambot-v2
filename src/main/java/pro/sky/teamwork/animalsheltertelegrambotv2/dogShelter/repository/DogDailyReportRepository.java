package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogDailyReport;

import java.time.LocalDate;
import java.util.List;

public interface DogDailyReportRepository extends JpaRepository<DogDailyReport, Long> {
    List<DogDailyReport> findDogDailyReportByDogCarerId(Long carerId);

    DogDailyReport findDogDailyReportByDogCarerIdAndReportDate(Long carerId, LocalDate reportDate);

    List<DogDailyReport> findDogDailyReportsByReportDate(LocalDate localDate);
}
