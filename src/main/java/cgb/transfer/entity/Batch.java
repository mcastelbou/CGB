package cgb.transfer.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Batch {

	/**
	 * L'identifiant du lot.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long batchNumber;

	/**
	 * La référence (date + numéro d'ordre) du lot.
	 */
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

	// Getters & Setters

	/**
	 * Getter de l'identifiant du lot.
	 * 
	 * @return L'identifiant du lot.
	 */
	public Long getBatchNumber() {
		return batchNumber;
	}

	/**
	 * Setter de l'identifiant du lot.
	 * @param batchNumber  Le nouvel identifiant 
	 */
	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
	}

	/**
	 * Getter de la référence du lot.
	 * @return  La référence du lot au format "Date-Numéro d'ordre"
	 */
	public String getRefBatch() {
		return refBatch;
	}

	/**
	 * Setter de la référence du lot.
	 * @param refBatch  La nouvelle référence du lot
	 */
	public void setRefBatch(String refBatch) {
		this.refBatch = refBatch;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<BatchTransfer> getTransferList() {
		return transferList;
	}

	public void setTransferList(List<BatchTransfer> transferList) {
		this.transferList = transferList;
	}

}
