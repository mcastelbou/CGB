package cgb.transfer.exception;

public class InvalidUnCheckableIbanException extends ExceptionInvalideIBAN {


	public enum FailureIbanInvalid{
		INCORRECT_CRC,
		UNVERIFIABLE_CRC
	};
	
	public InvalidUnCheckableIbanException(FailureIbanInvalid fii) {
		super(fii.name());
		// TODO Auto-generated constructor stub
	}
}
