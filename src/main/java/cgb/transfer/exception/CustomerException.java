package cgb.transfer.exception;

public class CustomerException extends Exception{
	private static final long serialVersionUID = 1L;
	
	public enum CustomerFailure {
		CUSTOMER_NOT_FOUND, INVALID_ACCOUNT
	}

	public CustomerException(CustomerFailure cf) {
		super(cf.name());
	}
}
