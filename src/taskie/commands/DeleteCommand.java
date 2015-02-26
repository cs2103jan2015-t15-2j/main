/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
// @author       A0097582N


package taskie.commands;

<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> 7d0825778eb4af3a67362765d5f6a05f592c16f5
import java.time.LocalTime;
import java.util.Calendar;

import taskie.models.CommandType;
import taskie.models.Task;

public class DeleteCommand implements ICommand {
	private String _taskName;
<<<<<<< HEAD
	private LocalDate _startDateToDelete;
	private LocalDate _endDateToDelete;
=======
	private LocalTime _startDateToDelete;
	private LocalTime _endDateToDelete;
>>>>>>> 7d0825778eb4af3a67362765d5f6a05f592c16f5
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

<<<<<<< HEAD
	public void setStartDateToDelete(LocalDate startDate) {
		_startDateToDelete = startDate;
	}

	public void setEndDateToDelete(LocalDate endDate) {
=======
	public void setStartDateToDelete(LocalTime startDate) {
		_startDateToDelete = startDate;
	}

	public void setEndDateToDelete(LocalTime endDate) {
>>>>>>> 7d0825778eb4af3a67362765d5f6a05f592c16f5
		_endDateToDelete = endDate;
	}

	public void setStartTimeToDelete(LocalTime startTime) {
		_startTimeToDelete = startTime;
	}

	public void setEndTimeToDelete(LocalTime endTime) {
		_endTimeToDelete = endTime;
	}
	
<<<<<<< HEAD
	public LocalDate getStartDateToDelete() {
		return _startDateToDelete;
	}

	public LocalDate getEndDateToDelete() {
=======
	public LocalTime getStartDateToDelete() {
		return _startDateToDelete;
	}

	public LocalTime getEndDateToDelete() {
>>>>>>> 7d0825778eb4af3a67362765d5f6a05f592c16f5
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
