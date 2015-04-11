//@author A0121555M
package taskie.exceptions;

public class NothingToRedoException extends Exception {
	private static final long serialVersionUID = -7444529190562177299L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.NOTHING_TO_REDO_EXCEPTION;

	public NothingToRedoException() {
		super(DEFAULT_MESSAGE);
	}
}
