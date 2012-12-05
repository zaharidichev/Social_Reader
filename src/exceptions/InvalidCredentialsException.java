package exceptions;

public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = 4423738627986751317L;

	public InvalidCredentialsException() {
		super();
	}

	public InvalidCredentialsException(String message) {
		super(message);
	}

}
