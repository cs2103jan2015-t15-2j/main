/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Taskie;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.TaskType;

public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE = 2;
	private int _taskIndex;
	private String _taskTitleToUpdate;
	private LocalDate _startDateToUpdate;
	private LocalTime _startTimeToUpdate;
	private LocalDate _endDateToUpdate;
	private LocalTime _endTimeToUpdate;

	private Boolean _isToUpdateTaskTitle = false;
	private Boolean _isToUpdateStartDate = false;
	private Boolean _isToUpdateStartTime = false;
	private Boolean _isToUpdateEndDate = false;
	private Boolean _isToUpdateEndTime = false;
	private CommandType _commandType = CommandType.UPDATE;

	private Logger _logger = Logger.getLogger(UpdateCommand.class.getName());

	public UpdateCommand() {
		_taskTitleToUpdate = null;
		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;

	}

	public UpdateCommand(int taskIndex) {
		_taskIndex = taskIndex;
		_taskTitleToUpdate = null;
		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;
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

	public void setTaskTitleToUpdate(String title) {
		_isToUpdateTaskTitle = true;
		_taskTitleToUpdate = title;
	}

	public String getTaskTitleToUpdate() {
		return _taskTitleToUpdate;
	}

	public LocalDate getStartDateToUpdate() {
		return _startDateToUpdate;
	}

	public void setStartDateToUpdate(LocalDate startDateToUpdate) {
		_isToUpdateStartDate = true;
		this._startDateToUpdate = startDateToUpdate;
	}

	public LocalTime getStartTimeToUpdate() {
		return _startTimeToUpdate;
	}

	public void setStartTimeToUpdate(LocalTime startTimeToUpdate) {
		_isToUpdateStartTime = true;
		this._startTimeToUpdate = startTimeToUpdate;
	}

	public LocalDate getEndDateToUpdate() {
		return _endDateToUpdate;
	}

	public void setEndDateToUpdate(LocalDate endDateToUpdate) {
		_isToUpdateEndDate = true;
		this._endDateToUpdate = endDateToUpdate;
	}

	public LocalTime getEndTimeToUpdate() {
		return _endTimeToUpdate;
	}

	public void setEndTimeToUpdate(LocalTime endTimeToUpdate) {
		_isToUpdateEndTime = true;
		this._endTimeToUpdate = endTimeToUpdate;
	}

	public Boolean isModifiedTaskTitle() {
		return _isToUpdateTaskTitle;
	}

	public Boolean isModifiedStartDate() {
		return _isToUpdateStartDate;
	}

	public Boolean isModifiedStartTime() {
		return _isToUpdateStartTime;
	}

	public Boolean isModifiedEndDate() {
		return _isToUpdateEndDate;
	}

	public Boolean isModifiedEndTime() {
		return _isToUpdateEndTime;
	}

	public void execute() {
		_logger.log(Level.INFO, "task Index: " + _taskIndex
				+ " taskTitleToUpdate: " + _taskTitleToUpdate + "\nstartdate: "
				+ _startDateToUpdate + " time: " + _startTimeToUpdate
				+ "\nendDate: " + _endDateToUpdate + " time:"
				+ _endTimeToUpdate);
		try {
			Task task = retrieveTaskToUpdateFromUI();

			Task updatedTask = updateTask(task);
			Taskie.Controller.getStorage().updateTask(task, updatedTask);
			Taskie.Controller.getUI().display(formatUpdateMsg(task));
		} catch (InvalidTaskException e) {
			Taskie.Controller.getUI().display(
					taskie.models.Messages.INVALID_TASK_NUM);
		} catch (InvalidCommandException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		} catch (TaskTypeNotSupportedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		} catch (TaskModificationFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		} catch (Exception e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
	}

	private Task updateTask(Task task) throws InvalidCommandException {
		Task updatedTask = new Task(task);
		if (this.isModifiedTaskTitle()) {
			if (_taskTitleToUpdate == null
					|| _taskTitleToUpdate.trim().equalsIgnoreCase("")) {
				throw new InvalidCommandException();
			} else {
				updatedTask.setTitle(this.getTaskTitleToUpdate());
			}
		}
		if (this.isModifiedStartDate()) {
			updatedTask.setStartDate(this.getStartDateToUpdate());
		}
		if (this.isModifiedStartTime()) {
			updatedTask.setStartTime(this.getStartTimeToUpdate());
		}
		if (this.isModifiedEndDate()) {
			updatedTask.setEndDate(this.getEndDateToUpdate());
		}
		if (this.isModifiedEndTime()) {
			updatedTask.setEndTime(this.getEndTimeToUpdate());
		}

		checkForTaskModelConsistency(updatedTask);
		updatedTask = checkForStartEndDateConsistency(task, updatedTask);
		return updatedTask;
	}

	// if startDateTime is modified, to beyond enddatetime, enddatetime would be
	// updated for consistency, and vice versa
	private Task checkForStartEndDateConsistency(Task task, Task updatedTask) {
		if (isModifiedStartDate() || isModifiedStartTime()) {
			Long taskDuration = task.getStartDateTime().until(
					task.getEndDateTime(), ChronoUnit.MINUTES);
			if (updatedTask.getStartDateTime().isAfter(updatedTask.getEndDateTime())) {
				updatedTask.setEndDateTime(updatedTask.getStartDateTime().plusMinutes(
						taskDuration));
			}
		}
		return updatedTask;
	}

	// task model consistency refers to datetime consistency. Time cannot be
	// without date and startdate cannot be without end date
	private void checkForTaskModelConsistency(Task task)
			throws InvalidCommandException {
		if (task.getStartDate() == null) {
			if (task.getStartTime() != null) {
				throw new InvalidCommandException(
						"task startdate cannot be null when task endtime is not null");
			}
		}
		if (task.getEndDate() == null) {
			if (task.getEndTime() != null) {
				throw new InvalidCommandException(
						"task enddate cannot be null when task end time is not null");
			}
		}
		if (task.getEndDateTime() == null) {
			if (task.getStartDateTime() != null) {
				throw new InvalidCommandException(
						"startdatetime must be null if enddatetime is null");
			}
		}
	}

	private String formatUpdateMsg(Task task) {
		String message = String.format(taskie.models.Messages.UPDATE_STRING,
				task.getTitle());
		if (this.isModifiedTaskTitle()) {
			message = message.concat("task title\n");
		}
		if (this.isModifiedStartDate()) {
			message = message.concat("start date\n");
		}
		if (this.isModifiedStartTime()) {
			message = message.concat("start time\n");
		}
		if (this.isModifiedEndDate()) {
			message = message.concat("end date\n");
		}
		if (this.isModifiedEndTime()) {
			message = message.concat("end time\n");
		}
		return message;

	}

	private Task retrieveTaskToUpdateFromUI() throws InvalidTaskException {
		Task task = Taskie.Controller.getUI().getTask(_taskIndex);
		return task;
	}

	// @author A0121555M
	public void undo() {
		// Taskie.Controller.executeCommand(new DeleteCommand(_task));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex + ",");
		sb.append("Title:" + _taskTitleToUpdate + ",");
		sb.append("StartDate:" + _startDateToUpdate + ",");
		sb.append("StartTime:" + _startTimeToUpdate + ",");
		sb.append("EndDate:" + _endDateToUpdate + ",");
		sb.append("EndTime:" + _endTimeToUpdate);
		return sb.toString();
	}

}
