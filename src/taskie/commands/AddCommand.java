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
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.Messages;

public class AddCommand implements ICommand {

	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";

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
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public void setStartDateTime(LocalDateTime startDateTime) {
		this.setStartDate(startDateTime.toLocalDate());
		this.setStartTime(startDateTime.toLocalTime());
	}

	public LocalDateTime getEndDateTime() {
		try {
			return LocalDateTime.of(_endDate,
					(_endTime == null) ? LocalTime.MAX : _endTime);
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public void setEndDateTime(LocalDateTime endDateTime) {
		this.setEndDate(endDateTime.toLocalDate());
		this.setEndTime(endDateTime.toLocalTime());
	}

	// @author A0097582N
	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {
		assert _taskName!=null;
		Task task = determineTaskTypeAndAdd();
		Taskie.Controller.getUI().display(formatAddMsg(task));
	}

	private String formatAddMsg(Task task) {
		 String taskType = Taskie.Controller.determineTaskType(task);
		 if(taskType.equalsIgnoreCase(FLOATING_TASKNAME)){
			 return String.format(taskie.models.Messages.ADD_FLOATING,task.getTitle());
		 }
		 if(taskType.equalsIgnoreCase(DEADLINED_TASKNAME)){
			 return String.format(taskie.models.Messages.ADD_DEADLINED,task.getTitle(),
					 task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		 }
		 else{
			 return String.format(taskie.models.Messages.ADD_TIMED,task.getTitle(),
					 task.getStartDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
					 task.getEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		 }
	}

	private Task determineTaskTypeAndAdd() {
		Task task = null;
		if (_startDate == null && _endDate == null) {	//has no start and end date
			task = new Task(_taskName);
			if (Taskie.Controller.getStorage() != null) {
				Taskie.Controller.getStorage().addFloatingTask(task);
			} else {
			}
		} else if (_startDate == null && _endDate != null) { //has only end date
			task = new Task(_taskName, _endDate, _endTime);
			if (Taskie.Controller.getStorage() != null) {
				Taskie.Controller.getStorage().addDeadlinedTask(task);
			} else {
			}
		} else {	//has both start and end date
			task = new Task(_taskName, _startDate, _startTime, _endDate,
					_endTime);
			if (Taskie.Controller.getStorage() != null) {
				Taskie.Controller.getStorage().addTimedTask(task);
			} else {
			}
		}
		return task;
	}
	
	//@author A0121555M
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
