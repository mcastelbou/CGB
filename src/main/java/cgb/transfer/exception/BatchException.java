package cgb.transfer.exception;

/**
 * Classe d'exception pour les erreurs lors de la récupération d'un rapport de
 * lot de virements.
 */
public class BatchException extends Exception {
	private static final long serialVersionUID = 1L;

	public enum BatchFailure {
		BATCH_NOT_FOUND
	}

	public BatchException(BatchFailure bf) {
		super(bf.name());
	}
}
