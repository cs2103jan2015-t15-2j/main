package taskie.commands;

import taskie.models.CommandType;

public class ExitCommand implements ICommand {
	private CommandType _commandType = CommandType.EXIT;

	public ExitCommand() {
	}

	public CommandType getCommandType() {
		return _commandType;
	}
}
