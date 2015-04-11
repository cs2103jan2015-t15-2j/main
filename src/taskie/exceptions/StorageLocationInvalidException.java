//@author A0121555M
package taskie.exceptions;

public class StorageLocationInvalidException extends Exception {
	private static final long serialVersionUID = -34643214965646L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.STORAGE_LOCATION_INVALID_EXCEPTION;

	public StorageLocationInvalidException() {
		super(DEFAULT_MESSAGE);
	}

	public StorageLocationInvalidException(String message) {
		super(message);
	}
}
