package cgb.transfer.entity;

import java.time.LocalDate;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long batchNumber;
	private String sourceAccount;
	private String description;
	private LocalDate startDate;
	private String status;

	@JsonManagedReference
	@OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BatchTransfer> transferList;

	public Long getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Long batchNumber) {
		this.batchNumber = batchNumber;
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
