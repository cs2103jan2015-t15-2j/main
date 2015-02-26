/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;

public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE = 2;
	private int _taskIndex;
	private LocalDate _startDateToUpdate;
	private LocalTime _startTimeToUpdate;
	private LocalDate _endDateToUpdate;
	private LocalTime _endTimeToUpdate;

	private Boolean _isModifiedStartDate = false;
	private Boolean _isModifiedStartTime = false;
	private Boolean _isModifiedEndDate = false;
	private Boolean _isModifiedEndTime = false;
	private CommandType _commandType = CommandType.UPDATE;

	public UpdateCommand() {

		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;

	}

	public UpdateCommand(int taskIndex) {
		_taskIndex = taskIndex;
		Task[] taskList = Taskie.UI.getCurrentTaskList();
		Task task = taskList[taskIndex];
		_startDateToUpdate = task.getStartDate();
		_startTimeToUpdate = task.getStartTime();
		_endDateToUpdate = task.getEndDate();
		_endTimeToUpdate = task.getEndTime();
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public int getTaskIndex() {
		return _taskIndex;
	}

	public void setTaskIndex(int taskIndex) {
		this._taskIndex = taskIndex;
	}

	public LocalDate getStartDateToUpdate() {
		return _startDateToUpdate;
	}

	public void setStartDateToUpdate(LocalDate startDateToUpdate) {
		this._startDateToUpdate = startDateToUpdate;
	}

	public LocalTime getStartTimeToUpdate() {
		return _startTimeToUpdate;
	}

	public void setStartTimeToUpdate(LocalTime startTimeToUpdate) {
		this._startTimeToUpdate = startTimeToUpdate;
	}

	public LocalDate getEndDateToUpdate() {
		return _endDateToUpdate;
	}

	public void setEndDateToUpdate(LocalDate endDateToUpdate) {
		this._endDateToUpdate = endDateToUpdate;
	}

	public LocalTime getEndTimeToUpdate() {
		return _endTimeToUpdate;
	}

	public void setEndTimeToUpdate(LocalTime endTimeToUpdate) {
		this._endTimeToUpdate = endTimeToUpdate;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
