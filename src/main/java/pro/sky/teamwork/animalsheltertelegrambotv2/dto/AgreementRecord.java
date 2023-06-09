package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import java.time.LocalDate;

public class AgreementRecord {
    private long id;
    private String number;
    private LocalDate conclusionDate;
    private long carerId;

    public AgreementRecord() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public long getCarerId() {
        return carerId;
    }

    public void setCarerId(long carerId) {
        this.carerId = carerId;
    }
}
