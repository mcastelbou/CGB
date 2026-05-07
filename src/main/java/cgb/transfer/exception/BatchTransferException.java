package cgb.transfer.exception;

public class BatchTransferException extends Exception {
private static final long serialVersionUID = 1L;
	
	public enum BatchTransferFailure {
		BATCH_TRANSFER_NOT_FOUND
	}

	public BatchTransferException(BatchTransferFailure btf) {
		super(btf.name());
	}
}
