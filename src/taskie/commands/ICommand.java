package taskie.commands;

import taskie.models.CommandType;

public interface ICommand {
	public CommandType getCommandType();
}
