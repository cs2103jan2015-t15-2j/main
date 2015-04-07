//@author A0121555M
package taskie.exceptions;

public class NothingToRedoException extends Exception {
	private static final long serialVersionUID = -7444529190562177299L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.NOTHINGTOREDOEXCEPTION_MSG;

	public NothingToRedoException() {
		super(DEFAULT_MESSAGE);
	}
}
