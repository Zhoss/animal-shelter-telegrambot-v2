package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Pet;

import java.util.Objects;

@Entity
@Table(name = "dogs")
public class Dog extends Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String breed;
    @Column(name = "coat_color", nullable = false)
    private String coatColor;
    @Column(nullable = false)
    private int age;
    private String features;
    @Column(name = "is_taken", nullable = false)
    private boolean taken;
    @Column(name = "on_probation", nullable = false)
    private boolean onProbation;

    public Dog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.isEmpty() && !name.isBlank()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Требуется указать кличку собаки");
        }
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        if (!breed.isEmpty() && !breed.isBlank()) {
            this.breed = breed;
        } else {
            throw new IllegalArgumentException("Требуется указать породу собаки");
        }
    }

    public String getCoatColor() {
        return coatColor;
    }

    public void setCoatColor(String coatColor) {
        if (!coatColor.isEmpty() && !coatColor.isBlank()) {
            this.coatColor = coatColor;
        } else {
            throw new IllegalArgumentException("Требуется указать окрас собаки");
        }
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

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public boolean isOnProbation() {
        return onProbation;
    }

    public void setOnProbation(boolean onProbation) {
        this.onProbation = onProbation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return getId() == dog.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Собака: " +
                "id = " + id +
                ", кличка = " + name +
                ", порода = " + breed +
                ", окрас = " + coatColor +
                ", возраст = " + age +
                ", особые приметы = " + features +
                ", взята опекуном = " + taken +
                ", на испытательном сроке = " + onProbation;
    }
}