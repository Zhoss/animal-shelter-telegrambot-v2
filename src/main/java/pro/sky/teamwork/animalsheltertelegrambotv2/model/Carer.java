package pro.sky.teamwork.animalsheltertelegrambotv2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import java.util.Objects;

@Entity
@Table(name = "carers")
public class Carer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "birth_year", nullable = false)
    private int birthYear;
    @Column(name = "phone_number", columnDefinition = "bpchar", length = 16, nullable = false)
    private String phoneNumber;
    private long chatId;
    @OneToOne
    @JoinColumn(name = "dog_id", referencedColumnName = "id")
    private Dog dog;
    @OneToOne
    @JoinColumn(name = "agreement_id", referencedColumnName = "id")
    private Agreement agreement;
    @OneToOne(mappedBy = "carer")
    private DailyReport dailyReport;

    public Carer() {

    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DailyReport getDailyReport() {
        return dailyReport;
    }

    public void setDailyReport(DailyReport dailyReport) {
        this.dailyReport = dailyReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carer carer = (Carer) o;
        return id == carer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Опекун: " +
                "id = " + id +
                ", ФИО = " + fullName +
                ", год рождения = " + birthYear +
                ", контактный телефон = " + phoneNumber;
    }
}
