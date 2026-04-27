package cgb.transfer.exception;

public class BatchException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public enum BatchFailure {
		BATCH_NOT_FOUND
	}
	
	public BatchException(BatchFailure bf) {
		super(bf.name());
	}
}
