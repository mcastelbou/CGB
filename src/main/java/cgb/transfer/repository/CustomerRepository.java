package cgb.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cgb.transfer.entity.Customer;

/**
 * Classe représentant la table des clients dans la DB. Possiblité de rajouter
 * des requêtes comme vu en cours (Cours_Java21).
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

}
