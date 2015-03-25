package taskie.exceptions;

public class TaskRetrievalFailedException extends Exception {
	private static final long serialVersionUID = -6851572204375503891L;
	private static final String DEFAULT_MESSAGE = "Failed to retrieve tasks";

	public TaskRetrievalFailedException() {
		super(DEFAULT_MESSAGE);
	}
	
	public TaskRetrievalFailedException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
}
