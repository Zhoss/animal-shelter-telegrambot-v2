package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import org.springframework.stereotype.Repository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.util.Objects;

/**
 * Клас описывающий поля для приема информации по <b> опекуну </b>, работает в Swagger.
 * <br>Параметры:
 * * <br> long id
 * * <br> String secondName;
 * * <br> String firstName;
 * * <br> String patronymic;
 * * <br> int age;
 * * <br> String phoneNumber;
 */
public class CarerRecord {
    private long id;
    private PetType petType;
    private String secondName;
    private String firstName;
    private String patronymic;
    private int age;
    private String phoneNumber;
    private String passportNumber;
    private long petId;

    public CarerRecord() {
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

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public long getPetId() {
        return petId;
    }

    public void setPetId(long petId) {
        this.petId = petId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarerRecord that = (CarerRecord) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
