package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

import pro.sky.teamwork.animalsheltertelegrambotv2.model.PetType;

import java.util.Objects;

/**
 * Клас описывающий поля для приема информации по <b> собаке </b> через Swagger.
 * <br>Параметры:
 * <br> long id
 * <br> String name;
 * <br> String breed;
 * <br> String coatColor;
 * <br> int age;
 * <br> String features;
 *
 */
public class PetRecord {
    private long id;
    private PetType petType;
    private String name;
    private String breed;
    private String coatColor;
    private int age;
    private String features;

    public PetRecord() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getCoatColor() {
        return coatColor;
    }

    public void setCoatColor(String coatColor) {
        this.coatColor = coatColor;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetRecord petRecord = (PetRecord) o;
        return getId() == petRecord.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
