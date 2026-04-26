package cgb.transfer.dto;

import cgb.transfer.entity.BatchTransfer;

/**
 * La classe de DTO d'un virement enregistré dans un lot.
 */
public class BatchTransferRequest {

	/**
	 * L'IBAN du compte bénéficiaire du virement.
	 */
	private String destinationAccount;

	/**
	 * Le montant transféré, peut être négatif.
	 */
	private Double amount;

	/**
	 * La description du virement.
	 */
	private String description;

	/**
	 * Constructeur par défaut du DTO.
	 */
	public BatchTransferRequest() {
		super();
	}

	/**
	 * Constructeur d'un DTO à partir d'un BatchTransfer.
	 * 
	 * @param bt Le virement à transformer en DTO.
	 */
	public BatchTransferRequest(BatchTransfer bt) {
		super();
		this.destinationAccount = bt.getDestinationAccount();
		this.amount = bt.getAmount();
		this.description = bt.getDescription();
	}

	/**
	 * Getter du compte bénéficiaire.
	 * 
	 * @return L'IBAN du compte bénéficiaire
	 */
	public String getDestinationAccount() {
		return destinationAccount;
	}

	/**
	 * Setter du compte bénéficiaire.
	 * 
	 * @param destinationAccountNumber le nouveau compte bénéficiaire du virement.
	 */
	public void setDestinationAccount(String destinationAccountNumber) {
		this.destinationAccount = destinationAccountNumber;
	}

	/**
	 * Getter du montant du virement.
	 * 
	 * @return Le montant du virement.
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * Setter du montant du virement.
	 * 
	 * @param amount Le nouveau montant du virement.
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * Getter de la description du virement.
	 * 
	 * @return La description du virement.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter de la description du virement.
	 * 
	 * @param description La nouvelle description du virement.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Méthode de bascule du DTO vers l'objet métier.
	 * 
	 * @return Un virement de type BatchTransfer.
	 */
	public BatchTransfer DTOtoBatchTransfer() {
		return new BatchTransfer(this);
	}

	/**
	 * Méthode de classe de bascule d'un objet métier vers un DTO.
	 * 
	 * @param bt L'objet métier de type BatchTransfer.
	 * @return Un DTO représentant l'objet métier initial.
	 */
	public static BatchTransferRequest BatchTransferToDTO(BatchTransfer bt) {
		return new BatchTransferRequest(bt);
	}

}
