//@author A0121555M
package taskie.exceptions;

public class UndoNotSupportedException extends Exception {
	private static final long serialVersionUID = -3885144311290567998L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.UNDONOTSUPPORTEDEXCEPTION_MSG;

	public UndoNotSupportedException() {
		super(DEFAULT_MESSAGE);
	}
}
