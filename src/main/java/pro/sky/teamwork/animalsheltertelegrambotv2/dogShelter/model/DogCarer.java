package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model;

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
@Table(name = "dog_carers")
public class DogCarer extends Carer {
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
    @JoinColumn(name = "dog_id", referencedColumnName = "id")
    private Dog dog;
    @OneToMany(mappedBy = "dogCarer", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<DogDailyReport> dogDailyReports;

    public DogCarer() {
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

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public List<DogDailyReport> getDailyReports() {
        return dogDailyReports;
    }

    public void setDogDailyReports(List<DogDailyReport> dogDailyReports) {
        this.dogDailyReports = dogDailyReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogCarer dogCarer = (DogCarer) o;
        return getId() == dogCarer.getId();
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
