package pro.sky.teamwork.animalsheltertelegrambotv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.teamwork.animalsheltertelegrambotv2.model.Agreement;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    Agreement findAgreementByCarer_Id(long carer_id);
}
