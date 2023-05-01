package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.Dog;

public interface DogRepository extends JpaRepository<Dog, Long> {

}
