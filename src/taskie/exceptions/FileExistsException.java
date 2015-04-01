package taskie.exceptions;

public class FileExistsException extends Exception {
	private static final long serialVersionUID = 3659402484486185953L;
	private static final String DEFAULT_MESSAGE = "A file already exists at this directory.";

	public FileExistsException() {
		super(DEFAULT_MESSAGE);
	}

	public FileExistsException(String string) {
		super(string);
	}
}
