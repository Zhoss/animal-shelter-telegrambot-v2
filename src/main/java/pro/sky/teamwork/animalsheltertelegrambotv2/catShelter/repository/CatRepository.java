package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.Cat;

public interface CatRepository extends JpaRepository<Cat, Long> {

}
