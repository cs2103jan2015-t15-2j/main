//@author A0121555M
package taskie.exceptions;

public class TaskTypeNotSupportedException extends Exception {
	private static final long serialVersionUID = -3383752749913995882L;
	private static final String DEFAULT_MESSAGE = "This task type is not supported.";

	public TaskTypeNotSupportedException() {
		super(DEFAULT_MESSAGE);
	}
}
