package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Carer;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "cat_carers")
public class CatCarer extends Carer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "birth_year", nullable = false)
    private int birthYear;
    @Column(name = "phone_number", columnDefinition = "bpchar", length = 16, nullable = false)
    private String phoneNumber;
    @Column(name = "chat_id", nullable = false)
    private long chatId;
    @Column(name = "passport_number")
    private String passportNumber;
    @OneToOne
    @JoinColumn(name = "cat_id", referencedColumnName = "id")
    private Cat cat;
    @OneToMany(mappedBy = "catCarer", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<CatDailyReport> catDailyReports;

    public CatCarer() {
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
        if (!fullName.isEmpty() && !fullName.isBlank()) {
            this.fullName = fullName;
        } else {
            throw new IllegalArgumentException("Требуется указать корректное ФИО опекуна");
        }
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
        if (!phoneNumber.isEmpty() && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Требуется указать корректный номер телефона опекуна");
        }
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber != null && !passportNumber.isEmpty() && !passportNumber.isBlank()) {
            this.passportNumber = passportNumber;
        } else {
            throw new IllegalArgumentException("Требуется указать корректный номер паспорта опекуна");
        }
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat Cat) {
        this.cat = Cat;
    }

    public List<CatDailyReport> getDailyReports() {
        return catDailyReports;
    }

    public void setCatDailyReports(List<CatDailyReport> catDailyReports) {
        this.catDailyReports = catDailyReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CatCarer CatCarer = (CatCarer) o;
        return getId() == CatCarer.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

        @Override
    public String toString() {
        return "Опекун: " +
                "id = " + id +
                ", ФИО = " + fullName +
                ", год рождения = " + birthYear +
                ", контактный телефон = " + phoneNumber +
                ", id чата = " + chatId +
                ", номер паспорта = " + passportNumber;
    }
}
