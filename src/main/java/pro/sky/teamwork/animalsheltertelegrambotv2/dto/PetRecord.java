package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

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
    private String petType;
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

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
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
}
