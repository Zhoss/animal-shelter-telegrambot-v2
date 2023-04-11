package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Dog;

public interface DogRepository extends JpaRepository<Dog, Long> {

}
