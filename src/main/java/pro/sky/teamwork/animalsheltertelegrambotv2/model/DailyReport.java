package pro.sky.teamwork.animalsheltertelegrambotv2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "daily_reports")
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String description;
    @Lob
    @Column(nullable = false)
    private byte[] photo;
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
    @OneToOne
    @JoinColumn(name = "carer_id", referencedColumnName = "id")
    private Carer carer;
    @OneToOne
    @JoinColumn(name = "dog_id", referencedColumnName = "id")
    private Dog dog;

    public DailyReport() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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

    public Carer getCarer() {
        return carer;
    }

    public void setCarer(Carer carer) {
        this.carer = carer;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyReport that = (DailyReport) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ежедневный отчет: " +
                "id = " + id +
                ", описание: " + description;
    }
}
