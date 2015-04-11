//@author A0121555M
package taskie.exceptions;

public class StorageMigrationFailedException extends Exception {
	private static final long serialVersionUID = -2824504424968793164L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.STORAGE_MIGRATION_FAILED_EXCEPTION;

	public StorageMigrationFailedException() {
		super(DEFAULT_MESSAGE);
	}

	public StorageMigrationFailedException(String message) {
		super(message);
	}

	public StorageMigrationFailedException(Throwable cause) {
		super(cause);
	}

	public StorageMigrationFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
