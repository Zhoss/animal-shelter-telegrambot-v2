package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.time.LocalDate;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Objects;


public class AgreementRecord {
    private long id;
    private PetType petType;
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

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgreementRecord that = (AgreementRecord) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "AgreementRecord{" +
                "id=" + id +
                ", petType=" + petType +
                ", number='" + number + '\'' +
                ", conclusionDate=" + conclusionDate +
                ", probationEndData=" + probationEndData +
                ", carerId=" + carerId +
                '}';
    }
}
