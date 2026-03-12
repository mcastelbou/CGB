package cgb.transfer.entity;

import jakarta.persistence.*;
import java.time.LocalDate;


/**
 * Classe permettant le mapping d'un tranfert entre la DB et l'API.
 */
@Entity
public class Transfer {
	
    /**
     * L'identifiant unique d'un tranfert; Auto-incrémenté.
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    /**
     * L'identifiant unique du compte dont le transfert provient.
     */
	private String sourceAccountNumber;
	
	/**
	 * L'identifiant unique du compte vers lequel le transfert termine.
	 */
    private String destinationAccountNumber;
    
    /**
     * Le montant du tranfert; peut être négatif.
     */
    private Double amount;
    
    /**
     * La date du transfert.
     */
    private LocalDate transferDate;
    
    /**
     * La description qui est associée au tranfert.
     */
    private String description;

    // Getters and Setters with lombok
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

}