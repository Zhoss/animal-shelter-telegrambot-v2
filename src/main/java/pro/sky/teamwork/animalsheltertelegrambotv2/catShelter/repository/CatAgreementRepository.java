package pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.catShelter.model.CatAgreement;

import java.util.Optional;

public interface CatAgreementRepository extends JpaRepository<CatAgreement, Long> {
    Optional<CatAgreement> findCatAgreementByCatCarer_Id(long carer_id);
}
