//@author A0121555M
package taskie.exceptions;

import taskie.commands.HelpCommand;
import taskie.models.CommandType;

public class InvalidCommandException extends Exception {
	private static final long serialVersionUID = -3383752749913995882L;
	private static final String DEFAULT_MESSAGE = taskie.models.Messages.INVALIDCOMMANDEXCEPTION_MSG;
	private HelpCommand _help;

	public InvalidCommandException() {
		super(DEFAULT_MESSAGE);
		_help = new HelpCommand(CommandType.HELP);
	}

	public InvalidCommandException(String string) {
		super(string);
		_help = new HelpCommand(CommandType.HELP);
	}
	
	//@author A0135137L
	public InvalidCommandException(CommandType type) {
		_help = new HelpCommand(type);
	}
	
	public HelpCommand getHelpCommand() {
		return _help;
	}
	
	
}
