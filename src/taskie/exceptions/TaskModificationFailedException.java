package taskie.exceptions;

public class TaskModificationFailedException extends Exception {
	private static final long serialVersionUID = 8682649329347945510L;
	private static final String DEFAULT_MESSAGE = "Modification of the task has failed";

	public TaskModificationFailedException() {
		super(DEFAULT_MESSAGE);
	}
	
	public TaskModificationFailedException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
}
