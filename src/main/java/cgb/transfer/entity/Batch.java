package cgb.transfer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import cgb.transfer.dto.BatchRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de mapping d'un lot de virements entre la base H2 et l'application
 * JAVA.
 */
@Entity
public class Batch {

	/**
	 * La référence (date + numéro d'ordre) du lot.
	 */
	@Id
	private String refBatch;

	/**
	 * Le compte source associé au lot.
	 */
	private String sourceAccount;

	/**
	 * La description du lot.
	 */
	private String description;

	/**
	 * La date du début du traitement du lot.
	 */
	private LocalDate startDate;

	/**
	 * Le status du lot (en cours de traitement ou traité).
	 */
	private String status;

	/**
	 * Lien de la liste des virements associés au lot.
	 */
	@JsonManagedReference
	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BatchTransfer> transferList = new ArrayList<BatchTransfer>();

	// Constructeurs
	public Batch() {	
	}
	
	public Batch(BatchRequest br) {
		super();
		this.refBatch = br.getRefBatch();
		this.sourceAccount = br.getSourceAccount();
		this.description = br.getDescription();
		this.status = "received";
	}
	
	// Getters & Setters

	/**
	 * Getter de la référence du lot.
	 * 
	 * @return La référence du lot au format "Date-Numéro d'ordre"
	 */
	public String getRefBatch() {
		return refBatch;
	}

	/**
	 * Setter de la référence du lot.
	 * 
	 * @param refBatch La nouvelle référence du lot
	 */
	public void setRefBatch(String refBatch) {
		this.refBatch = refBatch;
	}

	/**
	 * Getter du compte source du lot.
	 * 
	 * @return Le compte source associé au lot.
	 */
	public String getSourceAccount() {
		return sourceAccount;
	}

	/**
	 * Setter du compte source du lot.
	 * 
	 * @param sourceAccount L'IBAN du compte source
	 */
	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	/**
	 * Getter de la description du lot.
	 * 
	 * @return La description du lot
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter de la description du lot.
	 * 
	 * @param description La description du lot
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter de la date de début de traitement du lot de virements.
	 * 
	 * @return La date de début de traitement du lot
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Setter de la date de début de traitement du lot de virements.
	 * 
	 * @param startDate La date de début de traitement du lot
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter de l'état du lot.
	 * 
	 * @return L'état actuel du lot
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter de l'état du lot.
	 * 
	 * @param status Le nouvel état du lot
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Getter des virements associés au lot.
	 * 
	 * @return La liste des virements associés au lot
	 */
	public List<BatchTransfer> getTransferList() {
		return transferList;
	}

	/**
	 * Setter de la liste des virements associés au lot.
	 * 
	 * @param transferList La liste des virements à associer au lot
	 */
	public void setTransferList(List<BatchTransfer> transferList) {
		this.transferList = transferList;
	}

	/**
	 * Méthode d'ajout unique d'un virement à un lot.
	 * 
	 * @param transfer Le virement
	 */
	public void addTransfer(BatchTransfer transferRequest) {
		this.transferList.add(transferRequest);
	}
	
	public boolean hasDelays() {
		for (BatchTransfer batchTransfer : this.transferList) {
			if (batchTransfer.getStatus().equals("delayed")) {
				return true;
			}
		}
		return false;
	}
}
