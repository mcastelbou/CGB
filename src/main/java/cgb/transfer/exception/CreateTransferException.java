package cgb.transfer.exception;

/**
 * Classe d'exception pour les erreurs lors de la création d'un transfert.
 */
public class CreateTransferException extends TransferException {
	private static final long serialVersionUID = 1L;

	public enum TransferFailure {
		DESTINATION_ACCOUNT_NOT_FOUND, ILLEGAL_DATE, INSUFFICIENT_FUNDS, NEGATIVE_AMOUNT, SOURCE_ACCOUNT_NOT_FOUND
	}

	public CreateTransferException(TransferFailure tf) {
		super(tf.name());
	}

}
