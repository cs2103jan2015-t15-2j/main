package taskie.commands;

import java.util.Calendar;

import taskie.models.CommandType;
import taskie.models.ViewType;

public class ViewCommand implements ICommand {
	private Calendar _readDate;
	private CommandType _commandType = CommandType.VIEW;
	private ViewType _viewType;

	public ViewCommand(ViewType viewType) {
		_viewType = viewType;
		_readDate = null;
	}

	public ViewCommand(ViewType viewType, Calendar readDate) {
		_viewType = viewType;
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
