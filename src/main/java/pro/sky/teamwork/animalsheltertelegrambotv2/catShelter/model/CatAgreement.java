package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;

import java.time.LocalDate;
import java.util.Objects;
@Entity
@Table(name = "cat_agreements")
public class CatAgreement extends Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String number;
    @Column(name = "conclusion_date", nullable = false)
    private LocalDate conclusionDate;
    @Column(name = "probation_end_data", nullable = false)
    private LocalDate probationEndData;
    @OneToOne
    @JoinColumn(name = "cat_carer_id", referencedColumnName = "id")
    private CatCarer catCarer;

    public CatAgreement() {
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

    public CatCarer getCarer() {
        return catCarer;
    }

    public void setCarer(CatCarer CatCarer) {
        this.catCarer = CatCarer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatAgreement CatAgreement = (CatAgreement) o;
        return id == CatAgreement.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Соглашение о передаче кошки: " +
                "id = " + id +
                ", номер = " + number +
                ", дата заключения = " + conclusionDate;
    }
}
