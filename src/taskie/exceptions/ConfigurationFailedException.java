//@author A0135137L
package taskie.exceptions;

public class ConfigurationFailedException extends Exception {
	private static final long serialVersionUID = -34643214965646L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.CONFIGURATION_FAILED_EXCEPTION;

	public ConfigurationFailedException() {
		super(DEFAULT_MESSAGE);
	}

	public ConfigurationFailedException(String string) {
		super(string);
	}
}
