package pro.sky.teamwork.animalsheltertelegrambotv2.model;

public class Pet {
    public static final String CAT = "кошка";
    public static final String DOG = "собака";
    private long id;
    private String name;
    private String breed;
    private String coatColor;
    private int age;
    private String features;

    public Pet() {
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
            throw new IllegalArgumentException("Требуется указать кличку питомца");
        }
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        if (!breed.isEmpty() && !breed.isBlank()) {
            this.breed = breed;
        } else {
            throw new IllegalArgumentException("Требуется указать породу питомца");
        }
    }

    public String getCoatColor() {
        return coatColor;
    }

    public void setCoatColor(String coatColor) {
        if (!coatColor.isEmpty() && !coatColor.isBlank()) {
            this.coatColor = coatColor;
        } else {
            throw new IllegalArgumentException("Требуется указать окрас питомца");
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
}
