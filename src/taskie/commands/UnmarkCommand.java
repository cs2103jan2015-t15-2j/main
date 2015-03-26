/**
 * class representing an unmark command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import taskie.Taskie;
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Task;

public class UnmarkCommand implements ICommand {

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
				Taskie.Controller.getUI().display(formatUnmarkString());
			} else {
				Taskie.Controller.getUI().display("stub. Task has not been done.");
			}
			try {
				Taskie.Controller.getStorage().updateTask(_task, updatedTask);
			} catch (TaskTypeNotSupportedException e) {
				Taskie.Controller.getUI().display(e.getMessage());
			} catch (TaskModificationFailedException e) {
				Taskie.Controller.getUI().display(e.getMessage());
			}
		} catch (InvalidTaskException e) {
			Taskie.Controller.getUI().display(taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private String formatUnmarkString() {
		return String.format(taskie.models.Messages.UNMARK_STRING, _task.getTitle());
	}

	private Task retrieveTaskFromUI() throws InvalidTaskException {
		Task task = Taskie.Controller.getUI().getTask(_taskIndex);
		return task;
	}

	// @author A0121555M
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
