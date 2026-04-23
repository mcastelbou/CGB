package cgb.transfer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.Batch;

/**
 * Classe représentant la table des lots dans la DB. Possiblité de rajouter
 * des requêtes comme vu en cours (Cours_Java21).
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
	Optional<Batch> findByRefBatch(String refBatch);
}
