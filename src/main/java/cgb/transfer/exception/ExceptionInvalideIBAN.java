package cgb.transfer.exception;

public abstract class ExceptionInvalideIBAN extends Exception {
	private static final long serialVersionUID = 1L;

	public ExceptionInvalideIBAN(String message) {
		super(message);
	}

}
