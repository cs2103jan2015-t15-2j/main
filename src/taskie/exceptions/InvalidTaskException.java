//@author A0121555M
package taskie.exceptions;

public class InvalidTaskException extends Exception {
	private static final long serialVersionUID = 912925696346606858L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.INVALIDTASKEXCEPTION_MSG;

	public InvalidTaskException() {
		super(DEFAULT_MESSAGE);
	}

	public InvalidTaskException(String message) {
		super(message);
	}
}
