package cgb.transfer.exception;

/**
 * Classe d'exception des cas de mauvais format d'IBAN.
 */
public class InvalidIbanFormatException extends ExceptionInvalideIBAN {
	private static final long serialVersionUID = 1L;

	public enum FailureIbanFormat {
		INVALID_CHARACTER, INVALID_COUNTRY
	};

	public InvalidIbanFormatException(FailureIbanFormat fif) {
		super(fif.name());
		// TODO Auto-generated constructor stub
	}

	public InvalidIbanFormatException() {
		super("Format d'IBAN non reconnu.");
	}

}
