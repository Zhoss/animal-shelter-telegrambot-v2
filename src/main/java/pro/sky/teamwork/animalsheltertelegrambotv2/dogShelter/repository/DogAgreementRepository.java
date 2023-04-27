package pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.dogShelter.model.DogAgreement;

import java.util.Optional;

public interface DogAgreementRepository extends JpaRepository<DogAgreement, Long> {
    Optional<DogAgreement> findDogAgreementByDogCarer_Id(long carer_id);
}
