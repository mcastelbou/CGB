package cgb.transfer.dto;

import java.time.LocalDate;

import cgb.transfer.entity.BatchTransfer;

/**
 * La classe de DTO d'un transfert.
 */
public class TransferRequest {
	/**
	 * Le numéro de compte dont provient le transfert.
	 */
	private String sourceAccountNumber;
	/**
	 * Le numéro du compte ou termine le transfert.
	 */
	private String destinationAccountNumber;
	/**
	 * Le montant transféré; peut être négatif.
	 */
	private Double amount;
	/**
	 * La date du transfert.
	 */
	private LocalDate transferDate;
	/**
	 * La description que l'utilisateur à associé au transfert; peut être vide.
	 */
	private String description;

	public TransferRequest() {
		super();
	}

	public TransferRequest(String destinationAccountNumber, Double amount, String description) {
		super();
		this.destinationAccountNumber = destinationAccountNumber;
		this.amount = amount;
		this.description = description;
	}

	public String getSourceAccountNumber() {
		return sourceAccountNumber;
	}

	public void setSourceAccountNumber(String sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}

	public String getDestinationAccountNumber() {
		return destinationAccountNumber;
	}

	public void setDestinationAccountNumber(String destinationAccountNumber) {
		this.destinationAccountNumber = destinationAccountNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(LocalDate transferDate) {
		this.transferDate = transferDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean hasNoDate() {
		return this.transferDate == null;
	}
	
	public BatchTransfer DTOtoBatchTransfer() {
		return new BatchTransfer(destinationAccountNumber, amount, description);
	}
	
	static TransferRequest BatchTransferToDTO(BatchTransfer Bt) {
		return new TransferRequest(Bt.getDestinationAccount(), Bt.getAmount(), Bt.getDescription());
	}

}
