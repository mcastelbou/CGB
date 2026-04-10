package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.BatchTransfer;

/**
 * Classe représentant la table des tranferts par lots dans la DB. Possiblité de
 * rajouter des requêtes comme vu en cours (Cours_Java21).
 */
@Repository
public interface BatchTransferRepository extends JpaRepository<BatchTransfer, Long> {

}
