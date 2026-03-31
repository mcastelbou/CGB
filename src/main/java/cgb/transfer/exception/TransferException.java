package cgb.transfer.exception;

/**
 * Classe d'exception lancée lors d'une erreur concernant un transfert.
 */
public abstract class TransferException extends Exception {
	private static final long serialVersionUID = 1L;

	public TransferException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}