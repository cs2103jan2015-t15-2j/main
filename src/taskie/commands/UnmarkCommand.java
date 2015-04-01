/**
 * class representing an unmark command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Task;

public class UnmarkCommand extends AbstractCommand {

	private CommandType _commandType = CommandType.MARK;
	private int _taskIndex;
	private Task _task;

	//@author A0121555M
	public UnmarkCommand(int itemNumber) {
		_taskIndex = itemNumber;
	}

	//@author A0097582N
	public UnmarkCommand(Task task) {
		_task = task;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public Task getTask() {
		return _task;
	}

	public void execute() {
		try {
			_task = retrieveTaskFromUI();
			Task updatedTask = new Task(_task);

			if (_task.isDone()) {
				updatedTask.setTaskUndone();
				_controller.getUI().display(DisplayType.ERROR, formatUnmarkString());
			} else {
				_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.TASK_ALREADY_NOT_DONE);
			}
			try {
				_controller.getStorage().updateTask(_task, updatedTask);
			} catch (TaskTypeNotSupportedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			} catch (TaskModificationFailedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			}
		} catch (InvalidTaskException e) {
			_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private String formatUnmarkString() {
		return String.format(taskie.models.Messages.UNMARK_STRING, _task.getTitle());
	}

	private Task retrieveTaskFromUI() throws InvalidTaskException {
		Task task = _controller.getUI().getTask(_taskIndex);
		return task;
	}

	//@author A0121555M
	public void undo() {
		new MarkCommand(_taskIndex).execute();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex);
		return sb.toString();
	}
}
