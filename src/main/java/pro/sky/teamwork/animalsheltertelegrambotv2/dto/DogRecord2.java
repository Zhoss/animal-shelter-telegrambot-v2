package pro.sky.teamwork.animalsheltertelegrambotv2.dto;

public class DogRecord2 {
    private long id;
//    private String name;
    private boolean onProbation;

    public DogRecord2(long id, boolean onProbation) {
        this.id = id;
//        this.name = name;
        this.onProbation = onProbation;
    }

//    public DogRecord2() {
//
//    }

    public long getId() {
        return id;
    }

//    public String getName() {
//        return name;
//    }

    public boolean isOnProbation() {
        return onProbation;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public void setOnProbation(boolean onProbation) {
        this.onProbation = onProbation;
    }
}
