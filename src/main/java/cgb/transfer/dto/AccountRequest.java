package cgb.transfer.dto;

public class AccountRequest {

	private String accountNumber;
	private boolean belongsToCustomer;
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumbers) {
		this.accountNumber = accountNumbers;
	}
	public boolean isBelongsToCustomer() {
		return belongsToCustomer;
	}
	public void setBelongsToCustomer(boolean belongsToCustomer) {
		this.belongsToCustomer = belongsToCustomer;
	}
	
	
}
