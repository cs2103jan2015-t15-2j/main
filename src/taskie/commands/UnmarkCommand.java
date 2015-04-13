/**
 * class representing an unmark command.
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

public class UnmarkCommand extends AbstractCommand {
	private CommandType _commandType = CommandType.MARK;
	private Task _currentTask;

	private ArrayList<Task> _tasks = new ArrayList<Task>();
	private int[] _taskIndexes;

	//@author A0121555M
	public UnmarkCommand(int itemNumber) {
		_taskIndexes = new int[] { itemNumber };
	}

	public UnmarkCommand(int[] itemNumbers) {
		_taskIndexes = itemNumbers;
	}

	public UnmarkCommand(Task task) {
		_tasks.add(task);
	}

	public UnmarkCommand(Task[] tasks) {
		for (int x = 0; x < tasks.length; x++) {
			_tasks.add(tasks[x]);
		}
	}

	public UnmarkCommand(ArrayList<Task> tasks) {
		_tasks.addAll(tasks);
	}

	// @author A0097582N
	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		this.retrieveTasks();

		for (Task task : _tasks) {
			try {
				_currentTask = task;

				if (_currentTask.isDone()) {
					_currentTask.setTaskUndone();
					_controller.getUI().display(DisplayType.SUCCESS,
							formatUnmarkString());
					_controller.getStorage().updateTask(_currentTask,
							_currentTask);
				} else {
					_controller.getUI().display(DisplayType.ERROR,
							formatAlreadyUnmarkedString());
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

	private String formatAlreadyUnmarkedString() {
		return String.format(taskie.models.Messages.TASK_ALREADY_NOT_DONE,
				_currentTask.getTitle());
	}

	private String formatUnmarkString() {
		return String.format(taskie.models.Messages.UNMARK_STRING,
				_currentTask.getTitle());
	}

	// @author A0121555M
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
		new MarkCommand(_tasks).execute();

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");

		sb.append("TaskIndexes:" + Arrays.toString(_taskIndexes));
		return sb.toString();
	}
}
