/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;

public class DeleteCommand extends AbstractCommand {
	private ArrayList<Task> _tasks = new ArrayList<Task>();
	private int[] _taskIndexes;
	Logger _logger;

	private Task _currentTask;
	private Boolean _deleteStartDate = false;
	private Boolean _deleteStartTime = false;
	private Boolean _deleteEndDate = false;
	private Boolean _deleteEndTime = false;

	private CommandType _commandType = CommandType.DELETE;

	//@author A0121555M
	public DeleteCommand(int itemNumber) {
		_taskIndexes = new int[] { itemNumber };
	}

	public DeleteCommand(int[] itemNumbers) {
		_taskIndexes = itemNumbers;
	}

	public DeleteCommand(Task task) {
		_tasks.add(task);
	}

	public DeleteCommand(Task[] tasks) {
		for (int x = 0; x < tasks.length; x++) {
			_tasks.add(tasks[x]);
		}
	}

	//@author A0097582N
	public void setToDeleteStartDate() {
		_deleteStartDate = true;
		_deleteStartTime = true; // if startDate is to be deleted, startTime
									// will be deleted too
	}

	public Boolean canDeleteStartDate() {
		return _deleteStartDate;
	}

	public void setToDeleteStartTime() {
		_deleteStartTime = true;
	}

	public Boolean canDeleteStartTime() {
		return _deleteStartTime;
	}

	public void setToDeleteEndDate() {
		_deleteEndDate = true;
		_deleteEndTime = true; // if endDate is to be deleted, endTime will be
								// deleted too
	}

	public Boolean canDeleteEndDate() {
		return _deleteEndDate;
	}

	public void setToDeleteEndTime() {
		_deleteEndTime = true;
	}

	public Boolean canDeleteEndTime() {
		return _deleteEndTime;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public boolean execute() {
		_logger = Logger.getLogger(DeleteCommand.class.getName());
		_logger.log(Level.INFO,"CommandType: "+this.getCommandType()+"  taskindexes: "
		+_taskIndexes+"\ndeleteStartDate: "+_deleteStartDate+"  deleteStartTime: "
				+_deleteStartTime+"\ndeleteEndDate: "+_deleteEndDate+"  deleteEndTime: "+_deleteEndTime);
		this.retrieveTasks();
		boolean success = true;

		for (Task task : _tasks) {
			try {
				_currentTask = task;
				if (canDeleteStartDate() || canDeleteStartTime() || canDeleteEndDate() || canDeleteEndTime()) {
					// if either of these methods returned true, only task
					// fields are to be deleted.
					deleteTaskField(task);
					_controller.getUI().display(DisplayType.SUCCESS, formatDeleteTaskFieldString());
				} else {
					deleteTask();
					_controller.getUI().display(DisplayType.SUCCESS, formatDeleteTaskString());
				}
			} catch (TaskTypeNotSupportedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				success = false;
			} catch (TaskModificationFailedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				success = false;
			}
		}

		return success;
	}

	private void deleteTaskField(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		// taskfields are deleted by setting to them null;

		if (!canDeleteStartDate() && canDeleteEndDate()) {// switch start and
															// end date if del
															// end date;
			Task updatedTask = new Task(task.getTitle(), task.getStartDate(), task.getStartTime());
			_controller.getStorage().updateTask(task, updatedTask);
			return;
		}else if (canDeleteStartDate()&&canDeleteEndDate()) {
			Task updatedTask = new Task(task.getTitle());
			_controller.getStorage().updateTask(task, updatedTask);
			return;
		}else if (canDeleteStartDate()) {
			Task updatedTask = new Task(task.getTitle(), task.getEndDate(), task.getEndTime());
			_controller.getStorage().updateTask(task, updatedTask);
			return;
		}
		if (canDeleteStartTime()) {
			_logger.log(Level.INFO, "Deleting startTime.");
			Task updatedTask = new Task(task.getTitle(), task.getStartDate(), null, task.getEndDate(), task.getEndTime());
			_controller.getStorage().updateTask(task, updatedTask);
			task=updatedTask;
			
		}
		if (canDeleteEndTime()) {
			_logger.log(Level.INFO, "Deleting endTime.");
			Task updatedTask = new Task(task.getTitle(), task.getStartDate(), task.getStartTime(), task.getEndDate(), null);
			_controller.getStorage().updateTask(task, updatedTask);
		}

	}

	//@author A0121555M
	private void retrieveTasks() {
		if (_taskIndexes == null) {
			return;
		}

		for (int x = 0; x < _taskIndexes.length; x++) {
			try {
				int index = _taskIndexes[x];
				if (index == 0) {
					_tasks.add(_controller.getLastTask());
				} else {
					_tasks.add(_controller.getUI().getTask(index));
				}
			} catch (InvalidTaskException e) {
				_controller.getUI().display(DisplayType.ERROR, Messages.INVALID_TASK_NUM);
			} catch (TaskRetrievalFailedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			}
		}
	}

	private void deleteTask() throws TaskTypeNotSupportedException, TaskModificationFailedException {
		_controller.getStorage().deleteTask(_currentTask);
	}

	//@author A0097582N
	private String formatDeleteTaskString() {
		return String.format(taskie.models.Messages.DELETE_TASK, _currentTask.getTitle());
	}

	private String formatDeleteTaskFieldString() {
		StringBuffer fields = new StringBuffer();
		
		if (canDeleteStartDate()) {
			fields.append("Start date, ");
		}
		if (canDeleteStartTime()) {
			fields.append("Start time, ");
		}
		if (canDeleteEndDate()) {
			fields.append("End date, ");
		}
		if (canDeleteEndTime()) {
			fields.append("End time, ");
		}	
		
		return String.format(taskie.models.Messages.DELETE_TASK_FIELD, _currentTask.getTitle(), fields.substring(0, fields.length() - 2));
	}

	//@author A0121555M
	@Override
	public void undo() {
		for (Task task : _tasks) {
			new AddCommand(task).execute();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("DeleteStartDate:" + _deleteStartDate);
		sb.append("DeleteStartTime:" + _deleteStartTime);
		sb.append("DeleteEndDate:" + _deleteEndDate);
		sb.append("DeleteEndTime:" + _deleteEndTime);
		sb.append("TaskIndex:" + Arrays.toString(_taskIndexes));
		return sb.toString();
	}
}
