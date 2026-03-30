package cgb.transfer.entity;

import jakarta.persistence.*;


/**
 * Classe permettant le mapping d'un compte entre la DB et l'API.
 */
@Entity
public class Account {
	
    /**
     * L'identifiant unique d'un compte.
     */
	@Id
    private String accountNumber;
	
    /**
     * Le solde du compte.
     */
	private Double solde;

    // Getters and Setters obtenus grace à Data
	
	public Double getSolde() {
		return solde;
	}
	
	public void setSolde(Double solde) {
		this.solde = solde;
	}
	
    public String getAccountNumber() {
		return accountNumber;
	}
    
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}