//@author A0121555M
package taskie.exceptions;

public class FileExistsException extends Exception {
	private static final long serialVersionUID = 3659402484486185953L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.FILEEXISTSEXCEPTION_MSG;

	public FileExistsException() {
		super(DEFAULT_MESSAGE);
	}

	public FileExistsException(String string) {
		super(string);
	}

	public FileExistsException(Throwable cause) {
		super(cause);
	}
	
	public FileExistsException(String string, Throwable cause) {
		super(string, cause);
	}

}
