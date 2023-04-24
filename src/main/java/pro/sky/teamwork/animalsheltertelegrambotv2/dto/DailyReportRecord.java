package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import java.time.LocalDate;

public class DailyReportRecord {
    private long id;
    private LocalDate reportDate;
    private String dogDiet;
    private String dogHealth;
    private String dogBehavior;
    private long carerId;

    public DailyReportRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getCarerId() {
        return carerId;
    }

    public void setCarerId(long carerId) {
        this.carerId = carerId;
    }

}
