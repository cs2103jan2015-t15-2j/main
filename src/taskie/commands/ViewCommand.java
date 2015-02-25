package taskie.commands;

import java.util.Calendar;

import taskie.models.CommandType;

public class ViewCommand implements ICommand {
	private Calendar _readDate;
	private CommandType _commandType = CommandType.VIEW;

	public ViewCommand() {
		_readDate = null;
	}

	public ViewCommand(Calendar readDate) {
		_readDate = readDate;
	}

	public void setReadDate(Calendar readDate) {
		_readDate = readDate;
	}

	// return null if no read date is specified.
	public Calendar getReadDate() {
		return _readDate;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}
}
