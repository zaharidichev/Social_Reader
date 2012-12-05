package exceptions;

public class InvalidDateException extends Exception {


	private static final long serialVersionUID = 5391659893070915267L;

	public InvalidDateException() {
		super();
	}

	public InvalidDateException(String message) {
		super(message);
	}
}
