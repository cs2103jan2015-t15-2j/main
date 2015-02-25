/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */ 
// @author       A0097582N

package taskie.commands;

import java.time.LocalTime;
import java.util.Calendar;

import taskie.models.CommandType;

public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE = 2;

	private String _taskName;
	private LocalTime _startDateToUpdate;
	private LocalTime _endDateToUpdate;
	private LocalTime _startTimeToUpdate;
	private LocalTime _endTimeToUpdate;
	private CommandType _commandType = CommandType.UPDATE;

	public UpdateCommand() {
		_taskName = null;
		_startDateToUpdate=null;
		_endDateToUpdate=null;
		_startTimeToUpdate = null;
		_endTimeToUpdate = null;
	}

	public UpdateCommand(String taskName) {
		_taskName = taskName;
		_startDateToUpdate=null;
		_endDateToUpdate=null;
		_startTimeToUpdate = null;
		_endTimeToUpdate = null;
	}
	
	public void setTaskName(String taskName) {
		_taskName = taskName;
	}

	public void setStartDateToUpdate(LocalTime startTime) {
		_startTimeToUpdate = startTime;
	}

	public void setStartTimeToUpdate(LocalTime startTime) {
		_startTimeToUpdate = startTime;
	}
	
	public void setEndDateToUpdate(LocalTime endTime) {
		_endTimeToUpdate = endTime;
	}
	
	public void setEndTimeToUpdate(LocalTime endTime) {
		_endTimeToUpdate = endTime;
	}
	

	public String getTaskName() {
		return _taskName;
	}
	
	public LocalTime getStartDateToUpdate() {
		return _startDateToUpdate;
	}

	public LocalTime getEndDateToUpdate() {
		return _endDateToUpdate;
	}
	
	public LocalTime getStartTimeToUpdate() {
		return _startTimeToUpdate;
	}

	public LocalTime getEndTimeToUpdate() {
		return _endTimeToUpdate;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}

}
