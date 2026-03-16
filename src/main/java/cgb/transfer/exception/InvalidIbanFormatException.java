package cgb.transfer.exception;

public class InvalidIbanFormatException extends ExceptionInvalideIBAN{

	public enum FailureIbanFormat{
		INVALID_CHARACTER,
		INVALID_COUNTRY
	};
	
	public InvalidIbanFormatException(FailureIbanFormat fif) {
		super(fif.name());
		// TODO Auto-generated constructor stub
	}

}
