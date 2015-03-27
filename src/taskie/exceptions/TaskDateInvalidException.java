package taskie.exceptions;

public class TaskDateInvalidException extends Exception {
	private static final long serialVersionUID = -591075736010014405L;
	private static final String DEFAULT_MESSAGE = "Task time not set";

	public TaskDateInvalidException() {
		super(DEFAULT_MESSAGE);
	}
	
	public TaskDateInvalidException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
	
	public TaskDateInvalidException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TaskDateInvalidException(String message) {
		super(message);
	}
}
