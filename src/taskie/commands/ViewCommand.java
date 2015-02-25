package taskie.commands;

import java.time.LocalDateTime;

import taskie.models.CommandType;
import taskie.models.ViewType;

public class ViewCommand implements ICommand {
	private CommandType _commandType = CommandType.VIEW;
	private ViewType _viewType;
	
	private LocalDateTime _startDateTime;
	private LocalDateTime _endDateTime;

	public ViewCommand(ViewType viewType) {
		_viewType = viewType;
	}

	public LocalDateTime getStartDateTime() {
		return _startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		_startDateTime =  startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return _endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		_endDateTime =  endDateTime;
	}

	public CommandType getCommandType() {
		return _commandType;
	}
}
