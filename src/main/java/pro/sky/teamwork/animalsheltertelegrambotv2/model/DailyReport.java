package pro.sky.teamwork.animalsheltertelegrambotv2.model;

import java.time.LocalDate;

public class DailyReport {
    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private LocalDate reportDate;
    private String dogDiet;
    private String dogHealth;
    private String dogBehavior;

    public DailyReport() {
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
}
