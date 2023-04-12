package pro.sky.teamwork.animalsheltertelegrambotv2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @OneToMany(mappedBy = "carer")
    private List<DailyReport> dailyReports;

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

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public List<DailyReport> getDailyReports() {
        return dailyReports;
    }

    public void setDailyReports(List<DailyReport> dailyReports) {
        this.dailyReports = dailyReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carer carer = (Carer) o;
        return getId() == carer.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Carer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", birthYear=" + birthYear +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", chatId=" + chatId +
                ", dog=" + dog +
                ", agreement=" + agreement +
                ", dailyReports=" + dailyReports +
                '}';
    }

    //    @Override
//    public String toString() {
//        return "Опекун: " +
//                "id = " + id +
//                ", ФИО = " + fullName +
//                ", год рождения = " + birthYear +
//                ", контактный телефон = " + phoneNumber;
//    }
}
