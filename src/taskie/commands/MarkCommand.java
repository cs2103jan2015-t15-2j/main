package taskie.commands;

import taskie.models.CommandType;
import taskie.models.Task;

public class MarkCommand implements ICommand {
	private CommandType _commandType = CommandType.MARK;

	public MarkCommand(Task task) {
		
	}

	public CommandType getCommandType() {
		return _commandType;
	}

}
