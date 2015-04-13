/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N
package taskie.commands;

import java.util.ArrayList;
import java.util.Arrays;

import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;

public class MarkCommand extends AbstractCommand {
	private CommandType _commandType = CommandType.MARK;
	private Task _currentTask;

	private ArrayList<Task> _tasks = new ArrayList<Task>();
	private int[] _taskIndexes;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndexes = new int[] { itemNumber };
	}

	public MarkCommand(int[] itemNumbers) {
		_taskIndexes = itemNumbers;
	}

	public MarkCommand(Task task) {
		_tasks.add(task);
	}

	public MarkCommand(Task[] tasks) {
		for (int x = 0; x < tasks.length; x++) {
			_tasks.add(tasks[x]);
		}
	}

	public MarkCommand(ArrayList<Task> tasks) {
		_tasks.addAll(tasks);
	}

	//@author A0097582N
	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		this.retrieveTasks();

		for (Task task : _tasks) {
			try {
				_currentTask = task;

				if (_currentTask.isDone()) {
					_controller.getUI().display(DisplayType.ERROR,
							formatAlreadyMarkString());
				} else {
					_currentTask.setTaskDone();
					_controller.getUI().display(DisplayType.SUCCESS,
							formatMarkString());
					_controller.getStorage().updateTask(_currentTask,
							_currentTask);
				}

				_controller.setLastTask(_currentTask);
			} catch (TaskTypeNotSupportedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			} catch (TaskModificationFailedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			}
		}

		if (_tasks.size() > 1) {
			_controller.setLastTask(null);
		}
	}

	private String formatAlreadyMarkString() {
		return String.format(taskie.models.Messages.TASK_ALREADY_DONE,
				_currentTask.getTitle());
	}

	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING,
				_currentTask.getTitle());
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
				_controller.getUI().display(DisplayType.ERROR,
						Messages.INVALID_TASK_NUM);
			} catch (TaskRetrievalFailedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			}
		}
	}

	public void undo() {
		new UnmarkCommand(_tasks).execute();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + Arrays.toString(_taskIndexes));
		return sb.toString();
	}
}
