/**
 * class representing a update command. 
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
import taskie.models.Task;

public class MarkCommand extends AbstractCommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;
	private int _taskIndex;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndex = itemNumber;
	}

	//@author A0097582N
	public MarkCommand(Task task) {
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
				_controller.getUI().display("stub. Task already done");
			} else {
				updatedTask.setTaskDone();
				_controller.getUI().display(formatMarkString());
			}
			try {
				_controller.getStorage().updateTask(_task, updatedTask);
			} catch (TaskTypeNotSupportedException e) {
				_controller.getUI().display(e.getMessage());
			} catch (TaskModificationFailedException e) {
				_controller.getUI().display(e.getMessage());
			}
		} catch (InvalidTaskException e) {
			_controller.getUI().display(taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING, _task.getTitle());
	}

	private Task retrieveTaskFromUI() throws InvalidTaskException {
		Task task = _controller.getUI().getTask(_taskIndex);
		return task;
	}

	//@author A0121555M
	public void undo() {
		new UnmarkCommand(_taskIndex).execute();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex);
		return sb.toString();
	}
}
