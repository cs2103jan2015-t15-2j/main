/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
// @author       A0097582N


package taskie.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

import taskie.models.CommandType;
import taskie.models.Task;

public class DeleteCommand implements ICommand {
	private String _taskName;
	private LocalDate _startDateToDelete;
	private LocalDate _endDateToDelete;
	private LocalTime _startTimeToDelete;
	private LocalTime _endTimeToDelete;
	private CommandType _commandType = CommandType.DELETE;

	public DeleteCommand() {
		_taskName = null;
		_startTimeToDelete = null;
		_endTimeToDelete = null;
	}
	
	public DeleteCommand(Task task) {
		
	}

	public DeleteCommand(String taskName) {
		_taskName = taskName;
	}

	public void setTaskName(String taskName) {
		_taskName = taskName;
	}

	public String getTaskName() {
		return _taskName;
	}

	public void setStartDateToDelete(LocalDate startDate) {
		_startDateToDelete = startDate;
	}

	public void setEndDateToDelete(LocalDate endDate) {
		_endDateToDelete = endDate;
	}

	public void setStartTimeToDelete(LocalTime startTime) {
		_startTimeToDelete = startTime;
	}

	public void setEndTimeToDelete(LocalTime endTime) {
		_endTimeToDelete = endTime;
	}
	


	public LocalDate getStartDateToDelete() {
		return _startDateToDelete;
	}

	public LocalDate getEndDateToDelete() {
		return _endDateToDelete;
	}
	
	public LocalTime getStartTimeToDelete() {
		return _startTimeToDelete;
	}

	public LocalTime getEndTimeToDelete() {
		return _endTimeToDelete;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}
}
