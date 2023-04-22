package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AgreementRecord {
    private long id;
    private String number;
    private LocalDate conclusionDate;
    private LocalDate probationEndData;
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

    public LocalDate getProbationEndData() {
        return probationEndData;
    }

    public void setProbationEndData(LocalDate probationEndData) {
        this.probationEndData = probationEndData;
    }

    public long getCarerId() {
        return carerId;
    }

    public void setCarerId(long carerId) {
        this.carerId = carerId;
    }
}
