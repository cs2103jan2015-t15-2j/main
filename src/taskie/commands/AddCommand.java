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
import java.time.format.DateTimeFormatter;

import taskie.Taskie;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.TaskType;

public class AddCommand extends AbstractCommand {
	private String _taskName;
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private CommandType _commandType = CommandType.ADD;
	private Task _task;

	public AddCommand() {
		_taskName = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
	}

	public AddCommand(Task task) {
		_taskName = task.getTitle();
		_startDate = task.getStartDate();
		_startTime = task.getStartTime();
		_endDate = task.getEndDate();
		_endTime = task.getEndTime();
	}

	public AddCommand(String taskName, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
		super();
		_taskName = taskName;
		_startDate = startDate;
		_startTime = startTime;
		_endDate = endDate;
		_endTime = endTime;
	}

	public AddCommand(String taskName, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		super();
		_taskName = taskName;
		this.setStartDateTime(startDateTime);
		this.setEndDateTime(endDateTime);
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

	//@author A0121555M
	public LocalDateTime getStartDateTime() {
		try {
			return LocalDateTime.of(_startDate, (_startTime == null) ? LocalTime.MAX : _startTime);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		if (startDateTime == null) {
			this.setStartDate(null);
			this.setStartTime(null);
		} else {
			this.setStartDate(startDateTime.toLocalDate());
			this.setStartTime(startDateTime.toLocalTime());
		}
	}

	public LocalDateTime getEndDateTime() {
		try {
			return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX : _endTime);
		} catch (NullPointerException e) {
			return null;
		}
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		if (endDateTime == null) {
			this.setEndDate(null);
			this.setEndTime(null);
		} else {
			this.setEndDate(endDateTime.toLocalDate());
			this.setEndTime(endDateTime.toLocalTime());
		}
	}

	//@author A0097582N
	public CommandType getCommandType() {
		return _commandType;
	}

	//@author A0121555M
	public void execute() {
		assert _taskName != null;
		try {
			_task = new Task(_taskName, _startDate, _startTime, _endDate, _endTime);
			_controller.getStorage().addTask(_task);
			_controller.getUI().display(formatAddMsg(_task));
		} catch (TaskTypeNotSupportedException e) {
			_controller.getUI().display(e.getMessage());
		} catch (TaskModificationFailedException e) {
			_controller.getUI().display(e.getMessage());
		}
	}

	//@author A0097582N
	private String formatAddMsg(Task task) {
		TaskType type = task.getTaskType();
		if (type == TaskType.FLOATING) {
			return String.format(taskie.models.Messages.ADD_FLOATING, task.getTitle());
		} else if (type == TaskType.DEADLINE) {
			return String.format(taskie.models.Messages.ADD_DEADLINED, task.getTitle(), task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		} else {
			return String.format(taskie.models.Messages.ADD_TIMED, task.getTitle(), task.getStartDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		}
	}
	
	//@author A0097582N-unused
	//Reason for unused: Not necessary
	/*
	private Task determineTaskTypeAndAdd() {
		Task task = null;
		if (_startDate == null && _endDate == null) { // has no start and end date
			task = new Task(_taskName);
			Taskie.Controller.getStorage().addFloatingTask(task);
		} else if (_startDate == null && _endDate != null) { // has only end date
			task = new Task(_taskName, _endDate, _endTime);
			Taskie.Controller.getStorage().addDeadlinedTask(task);
		} else { // has both start and end date
			task = new Task(_taskName, _startDate, _startTime, _endDate, _endTime);
			Taskie.Controller.getStorage().addTimedTask(task);
		}
		return task;
	}
	*/

	//@author A0121555M
	@Override
	public void undo() {
		new DeleteCommand(_task).execute();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskName:" + _taskName + ",");

		try {
			sb.append("StartDateTime:" + this.getStartDateTime() + ",");
		} catch (NullPointerException e) {
			sb.append("StartDateTime:null,");
		}
		try {
			sb.append("EndDateTime:" + this.getEndDateTime());
		} catch (NullPointerException e) {
			sb.append("EndDateTime");
		}

		return sb.toString();
	}
}
