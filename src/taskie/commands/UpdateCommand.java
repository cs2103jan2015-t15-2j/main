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
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskDateInvalidException;
import taskie.exceptions.TaskDateNotSetException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;

public class UpdateCommand extends AbstractCommand {
	int NUM_ATTRIBUTE = 2;
	private Task _oldTask;
	private Task _task;

	private int _taskIndex;
	private String _taskTitleToUpdate = null;
	private LocalDate _startDateToUpdate = null;
	private LocalTime _startTimeToUpdate = null;
	private LocalDate _endDateToUpdate = null;
	private LocalTime _endTimeToUpdate = null;

	private Boolean _isToUpdateTaskTitle = false;
	private Boolean _isToUpdateStartDate = false;
	private Boolean _isToUpdateStartTime = false;
	private Boolean _isToUpdateEndDate = false;
	private Boolean _isToUpdateEndTime = false;
	private CommandType _commandType = CommandType.UPDATE;

	private Logger _logger = Logger.getLogger(UpdateCommand.class.getName());

	public UpdateCommand(Task task) {
		_task = task;
	}

	public UpdateCommand(int taskIndex) {
		_taskIndex = taskIndex;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void setTaskTitleToUpdate(String title) {
		_isToUpdateTaskTitle = true;
		_taskTitleToUpdate = title.trim();
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
		_logger.log(Level.INFO, "UPDATECOMMAND CONFIG: task Index: " + _taskIndex + " taskTitleToUpdate: " + _taskTitleToUpdate + "\nstartdate(bool): " + _startDateToUpdate + " " + _isToUpdateStartDate + "  time(bool): " + _startTimeToUpdate + " " + _isToUpdateStartTime + "\nendDate(bool): " + _endDateToUpdate + " " + _isToUpdateEndDate + " time(bool):" + _endTimeToUpdate + " " + _isToUpdateEndTime);

		try {
			if (_task == null) {
				_task = retrieveTaskToUpdate();
			}
			
			_logger.log(Level.INFO, "TASK FROM UI: " + _task.toString());
			_oldTask = new Task(_task);
			_task = updateTask(_task);
			_logger.log(Level.INFO, "UPDATED TASK: "+_task.toString());
			_controller.getStorage().updateTask(_task, _task);
			_controller.getUI().display(DisplayType.SUCCESS, formatUpdateMsg(_oldTask, _task));
		} catch (InvalidTaskException e) {
			_controller.getUI().display(DisplayType.ERROR, Messages.INVALID_TASK_NUM);
		} catch (InvalidCommandException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskTypeNotSupportedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskModificationFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskDateNotSetException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskDateInvalidException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskRetrievalFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (Exception e){
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		}
	}

	private Task updateTask(Task task) throws InvalidCommandException, TaskDateNotSetException, TaskDateInvalidException {
		task.setTitle(isModifiedTaskTitle() && !_taskTitleToUpdate.isEmpty() ? _taskTitleToUpdate : task.getTitle());
		task.setStartDateTime(isModifiedStartDate() ? _startDateToUpdate : task.getStartDate(), isModifiedStartTime() ? _startTimeToUpdate : task.getStartTime());
		task.setEndDateTime(isModifiedEndDate() ? _endDateToUpdate : task.getEndDate(), isModifiedEndTime() ? _endTimeToUpdate : task.getEndTime());
		return task;
	}

	private String formatUpdateMsg(Task oldTask, Task newTask) {
		String message = String.format(Messages.UPDATE_STRING, oldTask.getTitle());
		if (this.isModifiedTaskTitle()) {
			message = message.concat(String.format(Messages.TASK_TITLE, oldTask.getTitle(), newTask.getTitle()));
		}
		if (this.isModifiedStartDate() || this.isModifiedStartTime()) {
			if (oldTask.getStartDateTime() == null) {
				message = message.concat(String.format(Messages.START_DATE_TIME_NULL, _controller.getUI().formatDateTime(newTask.getStartDateTime())));
			} else {
				message = message.concat(String.format(Messages.START_DATE_TIME, _controller.getUI().formatDateTime(oldTask.getStartDateTime()), _controller.getUI().formatDateTime(oldTask.getStartDateTime())));
			}
		}
		if (this.isModifiedEndDate() || this.isModifiedEndTime()) {
			if (oldTask.getEndDateTime() == null) {
				message = message.concat(String.format(Messages.END_DATE_TIME_NULL, _controller.getUI().formatDateTime(newTask.getEndDateTime())));
			} else {
				message = message.concat(String.format(taskie.models.Messages.END_DATE_TIME, _controller.getUI().formatDateTime(oldTask.getEndDateTime()), _controller.getUI().formatDateTime(newTask.getEndDateTime())));
			}
		}
		return message;
	}

	private Task retrieveTaskToUpdate() throws InvalidTaskException, TaskRetrievalFailedException, InvalidCommandException {
		Task task = null;
		if (_taskIndex == 0) {
			task = _controller.getLastTask();
		} else {
			task = _controller.getUI().getTask(_taskIndex);
			_controller.setLastTask(task);
		}

		if (task == null) {
			throw new InvalidCommandException();
		}

		return task;
	}

	//@author A0121555M
	public void undo() {
		try {
			Task current = new Task(_task);
			_task.setTitle(isModifiedTaskTitle() && !_taskTitleToUpdate.isEmpty() ? _oldTask.getTitle() : _task.getTitle());
			_task.setStartDateTime(isModifiedStartDate() ? _oldTask.getStartDate() : _task.getStartDate(), isModifiedStartTime() ? _oldTask.getStartTime() : _task.getStartTime());
			_task.setEndDateTime(isModifiedEndDate() ? _oldTask.getEndDate() : _task.getEndDate(), isModifiedEndTime() ?  _oldTask.getEndTime() : _task.getEndTime());
			_controller.getStorage().updateTask(current, _oldTask);
			_controller.getUI().display(DisplayType.SUCCESS, formatUpdateMsg(current, _oldTask));
		} catch (TaskTypeNotSupportedException e) {
			_controller.getUI().display(DisplayType.ERROR, Messages.UNDO_FAILED);
		} catch (TaskModificationFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, Messages.UNDO_FAILED);
		} catch (TaskDateNotSetException e) {
			_controller.getUI().display(DisplayType.ERROR, Messages.UNDO_FAILED);
		} catch (TaskDateInvalidException e) {
			_controller.getUI().display(DisplayType.ERROR, Messages.UNDO_FAILED);
		}
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
