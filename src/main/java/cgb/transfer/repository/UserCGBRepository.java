package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.UserCGB;

/**
 * Classe représentant la table des utilisateurs dans la DB. Possiblité de
 * rajouter des requêtes comme vu en cours (Cours_Java21).
 */
@Repository
public interface UserCGBRepository extends JpaRepository<UserCGB, String> {

}
