package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;

import java.time.LocalDate;

public class AgreementRecord {

    private String number;
    private LocalDate conclusionDate;
    private Carer carer;


    public AgreementRecord() {

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

    public Carer getCarer() {
        return carer;
    }

    public void setCarer(Carer carer) {
        this.carer = carer;
    }
}
