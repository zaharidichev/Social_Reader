package exceptions;

public class ExistingUserException extends Exception {

	private static final long serialVersionUID = -1248992400995174852L;

	public ExistingUserException() {
		super();
	}

	public ExistingUserException(String message) {
		super(message);
	}
}
