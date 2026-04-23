package cgb.transfer.dto;

import cgb.transfer.entity.BatchTransfer;

public class BatchTransferRequest {

	/**
	 * Le numéro du compte ou termine le transfert.
	 */
	private String destinationAccount;
	/**
	 * Le montant transféré; peut être négatif.
	 */
	private Double amount;
	/**
	 * La date du transfert.
	 */
	private String description;

	public BatchTransferRequest(String destinationAccount, Double amount, String description) {
		super();
		this.destinationAccount = destinationAccount;
		this.amount = amount;
		this.description = description;
	}

	public String getDestinationAccount() {
		return destinationAccount;
	}

	public void setDestinationAccount(String destinationAccountNumber) {
		this.destinationAccount = destinationAccountNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BatchTransfer DTOtoBatchTransfer() {
		return new BatchTransfer(destinationAccount, amount, description);
	}

	static BatchTransferRequest BatchTransferToDTO(BatchTransfer Bt) {
		return new BatchTransferRequest(Bt.getDestinationAccount(), Bt.getAmount(), Bt.getDescription());
	}

}
