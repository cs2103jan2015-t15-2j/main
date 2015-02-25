package taskie.commands;

import taskie.models.CommandType;
import taskie.models.Task;

public class UnmarkCommand implements ICommand {
	private CommandType _commandType = CommandType.UNMARK;

	public UnmarkCommand(Task task) {
		
	}

	public CommandType getCommandType() {
		return _commandType;
	}

}
