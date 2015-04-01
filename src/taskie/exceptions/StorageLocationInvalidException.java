package taskie.exceptions;

public class StorageLocationInvalidException extends Exception {
	private static final long serialVersionUID = -34643214965646L;
	private static final String DEFAULT_MESSAGE = "Storage Location is invalid";

	public StorageLocationInvalidException() {
		super(DEFAULT_MESSAGE);
	}

	public StorageLocationInvalidException(String message) {
		super(message);
	}
}
