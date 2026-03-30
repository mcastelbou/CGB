package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cgb.transfer.entity.Account;

/**
 * Classe représentant la table des comptes dans la DB.
 * Possiblité de rajouter des requêtes comme vu en cours.
 * (il manque l'annotation de Repository)
 */
public interface AccountRepository extends JpaRepository<Account, String> {
}

