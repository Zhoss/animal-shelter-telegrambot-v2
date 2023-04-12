package pro.sky.teamwork.animalsheltertelegrambotv2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;
import java.util.Objects;
@Entity
@Table(name = "agreements")
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String number;
    @Column(name = "conclusion_date", nullable = false)
    private LocalDate conclusionDate;
    @OneToOne(mappedBy = "agreement")
    private Carer carer;

    public Agreement() {

    }

    public void setCarer(Carer carer) {
        this.carer = carer;
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

    public Carer getCarer() {
        return carer;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agreement agreement = (Agreement) o;
        return id == agreement.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Соглашение о передаче собаки: " +
                "id = " + id +
                ", номер = " + number +
                ", дата заключения = " + conclusionDate;
    }
}
