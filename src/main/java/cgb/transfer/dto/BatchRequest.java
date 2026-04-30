package cgb.transfer.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cgb.transfer.entity.Batch;
import cgb.transfer.entity.BatchTransfer;

/**
 * La classe de DTO d'un lot de virements.
 */
public class BatchRequest {

	/**
	 * L'identifiant du lot au format AAAA-MM-JJ-NUMERO_D_ORDRE.
	 */
	private String refBatch;
	/**
	 * Le compte source de l'ensemble des virements du lot.
	 */
	private String sourceAccount;
	/**
	 * La description associée au lot de virements.
	 */
	private String description;
	/**
	 * La date de début du traitement du lot.
	 */
	private LocalDate startDate;
	/**
	 * L'état du lot ('received' si reçu, 'completed' si le traitement est fini.
	 */
	private String status;
	/**
	 * La liste des virements associés au lot.
	 */
	private ArrayList<BatchTransferRequest> transfers;

	public BatchRequest() {
	}
	
	public BatchRequest(Batch batch) {
		 super();
		 this.startDate = batch.getStartDate();
		 this.description = batch.getDescription();
		 this.sourceAccount = batch.getSourceAccount();
		 this.status = batch.getStatus();
		 
		 ArrayList<BatchTransferRequest> btrList = new ArrayList<BatchTransferRequest>();
		 for (BatchTransfer bt : batch.getTransferList()) {
			 BatchTransferRequest btr = BatchTransferRequest.BatchTransferToDTO(bt);
			 btrList.add(btr);
		 }
		 this.transfers = btrList;
	 }
	
	/**
	 * Getter de l'identifiant du lot.
	 * 
	 * @return L'identifiant du lot
	 */
	public String getRefBatch() {
		return refBatch;
	}

	/**
	 * Setter de l'identifiant du lot
	 * 
	 * @param refBatch Le nouvel identifiant du lot.
	 */
	public void setRefBatch(String refBatch) {
		this.refBatch = refBatch;
	}

	/**
	 * Getter du compte source du lot.
	 * 
	 * @return Le compte source du lot.
	 */
	public String getSourceAccount() {
		return sourceAccount;
	}

	/**
	 * Setter du compte source du lot.
	 * 
	 * @param sourceAccount Le nouveau compte source du lot.
	 */
	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	/**
	 * Getter de la description du lot.
	 * 
	 * @return La description du lot.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter de la description du lot.
	 * 
	 * @param description La nouvelle description du lot.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter de la date de début de traitement du lot.
	 * 
	 * @return La date de début de traitement du lot.
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Setter de la date de début de traitement du lot.
	 * 
	 * @param startDate La date de début de traitement du lot.
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter de l'état du lot.
	 * 
	 * @return L'état actuel du lot.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Setter de l'état du lot.
	 * 
	 * @param status Le nouvel état du lot.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Getter de la liste des virements associés à un lot.
	 * 
	 * @return La liste des virements associés au lot.
	 */
	public List<BatchTransferRequest> getTransfers() {
		return transfers;
	}

	/**
	 * Setter de la liste des virements associés à un lot.
	 * 
	 * @param transferList La liste des virements à associer au lot.
	 */
	public void setTransfers(ArrayList<BatchTransferRequest> transferList) {
		this.transfers = transferList;
	}
	
	/**
	 * Méthode de bascule du DTO vers l'objet métier.
	 * 
	 * @return Un lot de virements de type Batch.
	 */
	public Batch DTOtoBatch() {
		return new Batch(this);
	}

	/**
	 * Méthode de classe de bascule d'un objet métier vers un DTO.
	 * 
	 * @param bt L'objet métier de type BatchTransfer.
	 * @return Un DTO représentant l'objet métier initial.
	 */
	public static BatchRequest BatchToDTO(Batch batch) {
		return new BatchRequest(batch);
	}

}