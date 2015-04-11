//@author A0121555M
package taskie.exceptions;

public class TaskModificationFailedException extends Exception {
	private static final long serialVersionUID = 8682649329347945510L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.TASK_MODIFICATION_FAILED_EXCEPTION;

	public TaskModificationFailedException() {
		super(DEFAULT_MESSAGE);
	}
	
	public TaskModificationFailedException(Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}
}
