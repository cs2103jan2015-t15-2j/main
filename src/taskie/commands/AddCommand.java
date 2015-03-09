package taskie.commands;

/**
 * class representing an add command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;

public class AddCommand implements ICommand {
	private String _taskName;
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private CommandType _commandType = CommandType.ADD;

	public AddCommand() {
		_taskName = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
	}

	public String getTaskName() {
		return _taskName;
	}

	public void setTaskName(String _taskName) {
		this._taskName = _taskName;
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate _startDate) {
		this._startDate = _startDate;
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public void setStartTime(LocalTime _startTime) {
		this._startTime = _startTime;
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void setEndDate(LocalDate _endDate) {
		this._endDate = _endDate;
	}

	public LocalTime getEndTime() {
		return _endTime;
	}

	public void setEndTime(LocalTime _endTime) {
		this._endTime = _endTime;
	}

	// @author A0121555M
	public LocalDateTime getStartDateTime() {
		try {
			return LocalDateTime.of(_startDate,
					(_startTime == null) ? LocalTime.MAX : _startTime);
		} catch ( NullPointerException e ) {
			return null;
		}
	}

	public LocalDateTime getEndDateTime() {
		try {
			return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX
					: _endTime);
		} catch ( NullPointerException e ) {
			return null;
		}
	}

	// @author A0097582N
	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {
		Task task = determineTaskTypeAndAdd();
		Taskie.UI.display(formatAddMsg(task));
	}

	private String formatAddMsg(Task task) {
		return String.format("STUB MSG_ Task Title:%s Task Time:%s",
				task.getTitle(),
				Taskie.Controller.formatTime(task.getStartDate(), task.getStartTime()));
	}



	private Task determineTaskTypeAndAdd() {
		Task task = null;
		if (_startDate == null && _endDate == null) {
			task = new Task(_taskName);
			if(Taskie.Storage!=null){
			Taskie.Storage.addFloatingTask(task);
			}else{}
		} else if (_startDate == null && _endDate != null) {
			task = new Task(_taskName, _endDate, _endTime);
			if(Taskie.Storage!=null){
			Taskie.Storage.addDeadlinedTask(task);
			}else{}
		} else {
			task = new Task(_taskName, _startDate, _startTime, _endDate,
					_endTime);
			if(Taskie.Storage!=null){
			Taskie.Storage.addTimedTask(task);
			}else{}
		}
		return task;

	}

}
