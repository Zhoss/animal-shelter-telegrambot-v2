package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.time.LocalDate;

public class DailyReportRecord {
    private long id;
    private PetType petType;
    private LocalDate reportDate;
    private String petDiet;
    private String petHealth;
    private String petBehavior;
    private long carerId;

    public DailyReportRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getPetDiet() {
        return petDiet;
    }

    public void setPetDiet(String petDiet) {
        this.petDiet = petDiet;
    }

    public String getPetHealth() {
        return petHealth;
    }

    public void setPetHealth(String petHealth) {
        this.petHealth = petHealth;
    }

    public String getPetBehavior() {
        return petBehavior;
    }

    public void setPetBehavior(String petBehavior) {
        this.petBehavior = petBehavior;
    }

    public long getCarerId() {
        return carerId;
    }

    public void setCarerId(long carerId) {
        this.carerId = carerId;
    }

}
