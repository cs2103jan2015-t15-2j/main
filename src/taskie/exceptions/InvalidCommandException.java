//@author A0121555M
package taskie.exceptions;

public class InvalidCommandException extends Exception {
	private static final long serialVersionUID = -3383752749913995882L;
	private static final String DEFAULT_MESSAGE = "The command could not be parsed";

	public InvalidCommandException() {
		super(DEFAULT_MESSAGE);
	}
}
