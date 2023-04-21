package pro.sky.teamwork.animalsheltertelegrambotv2.model;

import java.time.LocalDate;

public class Agreement {
    private long id;
    private String number;
    private LocalDate conclusionDate;
    private LocalDate probationEndData;

    public Agreement() {
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
        if (!number.isEmpty() && !number.isBlank()) {
            this.number = number;
        } else {
            throw new IllegalArgumentException("Требуется указать корректный номер договора");
        }
    }

    public LocalDate getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDate conclusionDate) {
        if (!conclusionDate.isBefore(LocalDate.now())) {
            this.conclusionDate = conclusionDate;
        } else {
            throw new IllegalArgumentException("Договор не может быть заключен с прошедшей датой");
        }
    }

    public LocalDate getProbationEndData() {
        return probationEndData;
    }

    public void setProbationEndData(LocalDate probationEndData) {
        this.probationEndData = probationEndData;
    }
}
