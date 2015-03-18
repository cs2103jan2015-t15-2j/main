//@author A0121555M
package taskie.exceptions;

public class InvalidTaskException extends Exception {
	private static final long serialVersionUID = 912925696346606858L;
	private static final String DEFAULT_MESSAGE = "The task is not valid";

	public InvalidTaskException() {
		super(DEFAULT_MESSAGE);
	}

	public InvalidTaskException(String message) {
		super(message);
	}
}
