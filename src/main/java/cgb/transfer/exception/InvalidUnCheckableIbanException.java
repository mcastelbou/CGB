package cgb.transfer.exception;

/**
 * Classe d'exception des cas de mauvais CRC
 */
public class InvalidUnCheckableIbanException extends ExceptionInvalideIBAN {
	private static final long serialVersionUID = 1L;

	public enum FailureIbanInvalid {
		INCORRECT_CRC, UNVERIFIABLE_CRC
	};

	public InvalidUnCheckableIbanException(FailureIbanInvalid fii) {
		super(fii.name());
		// TODO Auto-generated constructor stub
	}

	public InvalidUnCheckableIbanException() {
		super("Nous n'avons pas pu vérifier l'IBAN fourni.");
	}
}
