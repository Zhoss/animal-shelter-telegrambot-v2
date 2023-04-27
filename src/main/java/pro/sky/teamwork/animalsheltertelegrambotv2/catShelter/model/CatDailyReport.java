package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model;

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
@Table(name = "cat_daily_reports")
public class CatDailyReport extends DailyReport {
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
    @Column(name = "cat_diet", nullable = false)
    private String catDiet;
    @Column(name = "cat_health", nullable = false)
    private String catHealth;
    @Column(name = "cat_behavior", nullable = false)
    private String catBehavior;
    @ManyToOne
    @JoinColumn(name = "cat_carer_id", referencedColumnName = "id")
    @JsonBackReference
    private CatCarer catCarer;

    public CatDailyReport() {
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

    public String getCatDiet() {
        return catDiet;
    }

    public void setCatDiet(String CatDiet) {
        this.catDiet = CatDiet;
    }

    public String getCatHealth() {
        return catHealth;
    }

    public void setCatHealth(String CatHealth) {
        this.catHealth = CatHealth;
    }

    public String getCatBehavior() {
        return catBehavior;
    }

    public void setCatBehavior(String CatBehavior) {
        this.catBehavior = CatBehavior;
    }

    public CatCarer getCarer() {
        return catCarer;
    }

    public void setCarer(CatCarer CatCarer) {
        this.catCarer = CatCarer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatDailyReport that = (CatDailyReport) o;
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
                ", рацион кошки = " + catDiet +
                ", общее самочувствие кошки = " + catHealth +
                ", изменение в поведении кошки = " + catBehavior;
    }
}
