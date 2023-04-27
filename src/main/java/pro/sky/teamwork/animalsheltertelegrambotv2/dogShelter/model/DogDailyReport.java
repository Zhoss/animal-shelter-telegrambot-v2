package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.DailyReport;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс описывающий логику и поля DailyReport
 */
@Entity
@Table(name = "dog_daily_reports")
public class DogDailyReport extends DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_path", nullable = false)
    private String filePath;
    @Column(name = "file_size", nullable = false)
    private long fileSize;
    @Column(name = "media_type", nullable = false)
    private String mediaType;
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    @Column(name = "dog_diet", nullable = false)
    private String dogDiet;
    @Column(name = "dog_health", nullable = false)
    private String dogHealth;
    @Column(name = "dog_behavior", nullable = false)
    private String dogBehavior;
    @ManyToOne
    @JoinColumn(name = "dog_carer_id", referencedColumnName = "id")
    @JsonBackReference
    private DogCarer dogCarer;

    public DogDailyReport() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getDogDiet() {
        return dogDiet;
    }

    public void setDogDiet(String dogDiet) {
        this.dogDiet = dogDiet;
    }

    public String getDogHealth() {
        return dogHealth;
    }

    public void setDogHealth(String dogHealth) {
        this.dogHealth = dogHealth;
    }

    public String getDogBehavior() {
        return dogBehavior;
    }

    public void setDogBehavior(String dogBehavior) {
        this.dogBehavior = dogBehavior;
    }

    public DogCarer getCarer() {
        return dogCarer;
    }

    public void setCarer(DogCarer dogCarer) {
        this.dogCarer = dogCarer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogDailyReport that = (DogDailyReport) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ежедневный отчет: " +
                "id = " + id +
                ", дата отчет = " + reportDate +
                ", рацион собаки = " + dogDiet +
                ", общее самочувствие собаки = " + dogHealth +
                ", изменение в поведении собаки = " + dogBehavior;
    }
}
