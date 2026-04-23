package cgb.transfer.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatchRequest {

//	private String batchNumber;
	private String refBatch;
	private String sourceAccount;
	private String description;
	private LocalDate startDate;
	private String status;
	private ArrayList<TransferRequest> transfers;

//	public String getBatchNumber() {
//		return batchNumber;
//	}
//
//	public void setBatchNumber(String batchNumber) {
//		this.batchNumber = batchNumber;
//	}

	public String getRefBatch() {
		return refBatch;
	}

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

	public List<TransferRequest> getTransfers() {
		return transfers;
	}

	public void setTransfers(ArrayList<TransferRequest> transferList) {
		this.transfers = transferList;
	}

}