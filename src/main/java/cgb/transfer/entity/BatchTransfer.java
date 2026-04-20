package cgb.transfer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

/**
 * Classe de mapping d'un virement de lot entre la base H2 et l'application
 * JAVA.
 */
@Entity
public class BatchTransfer {

	/**
	 * L'identifiant du virement.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * L'identifiant du lot auquel est associé le virement.
	 */
	@JsonBackReference
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "batch_id")
	private Batch batch;

	/**
	 * L'IBAN du compte de destination.
	 */
	private String destinationAccount;

	/**
	 * Le montant du virement.
	 */
	private Double amount;

	/**
	 * La description du virement.
	 */
	private String description;

	/**
	 * La date à laquelle a été complété le virement.
	 */
	private LocalDate completionDate;

	/**
	 * L'état du virement.
	 */
	private String status;

	/**
	 * Getter de l'identifiant relatif du virement.
	 * 
	 * @return L'identifiant du virement
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter de l'identifiant relatif du virement.
	 * 
	 * @param id La partie relative de l'identifiant du virement
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter de l'identifiant du lot auquel est associé le virement
	 * 
	 * @return L'identifiant du lot
	 */
	public Batch getBatch() {
		return batch;
	}

	/**
	 * Setter de l'identifiant du lot auquel doit être associé le virement.
	 * 
	 * @param batch L'identifiant du lot
	 */
	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	/**
	 * Getter du compte de destination du virement.
	 * 
	 * @return L'IBAN du compte de destination
	 */
	public String getDestinationAccount() {
		return destinationAccount;
	}

	/**
	 * Setter du compte de destination du virement
	 * 
	 * @param destinationAccount L'IBAN du compte de destination
	 */
	public void setDestinationAccount(String destinationAccount) {
		this.destinationAccount = destinationAccount;
	}

	/**
	 * Getter du montant du virement.
	 * 
	 * @return Le montant du virement
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * Setter du montant du virement.
	 * 
	 * @param amount Le montant du virement
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * Getter de la description du virement.
	 * 
	 * @return La description du virement
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter de la description du virement.
	 * 
	 * @param description La nouvelle description du virement
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter de la date à laquelle le virement a été complété.
	 * 
	 * @return La date de completion du virement
	 */
	public LocalDate getCompletionDate() {
		return completionDate;
	}

	/**
	 * Setter de la date à laquelle le virement a été complété.
	 * 
	 * @param completionDate La date de completion du virement
	 */
	public void setCompletionDate(LocalDate completionDate) {
		this.completionDate = completionDate;
	}

	/**
	 * Setter de la date à laquelle le virement a été complété. Ici le calcul de la
	 * date est automatisé pour empêcher les virements antidatés.
	 */
	public void setCompletionDate() {
		this.completionDate = LocalDate.now();
	}

	/**
	 * Getter de l'état du virement.
	 * 
	 * @return L'état du virement
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter de l'état du virement.
	 * 
	 * @param status Le nouvel état du virement
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
