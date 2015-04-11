//@author A0121555M
package taskie.exceptions;

public class TaskDateNotSetException extends Exception {
	private static final long serialVersionUID = -591075736010014405L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.TASK_DATE_NOT_SET_EXCEPTION;

	public TaskDateNotSetException() {
		super(DEFAULT_MESSAGE);
	}
	
	public TaskDateNotSetException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
	
	public TaskDateNotSetException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TaskDateNotSetException(String message) {
		super(message);
	}
}
