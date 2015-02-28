/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.commands;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;

public class DeleteCommand implements ICommand {
	private String _taskName;
	private Boolean _DeleteStartDate;
	private Boolean _DeleteStartTime;
	private Boolean _DeleteEndDate;
	private Boolean _DeleteEndTime;

	private CommandType _commandType = CommandType.DELETE;

	public DeleteCommand() {
		_taskName = null;
		_DeleteStartDate = false;
		_DeleteStartTime = false;
		_DeleteEndDate = false;
		_DeleteEndTime = false;

	}

	public DeleteCommand(int taskId) {
		Task[] tasks = Taskie.UI.getCurrentTaskList();
		Task task = tasks[taskId];
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

	public void setToDeleteStartDate() {
		_DeleteStartDate = true;
	}

	public Boolean canDeleteStartDate() {
		return _DeleteStartDate;
	}

	public void setToDeleteStartTime() {
		_DeleteStartTime = true;
	}

	public Boolean canDeleteStartTime() {
		return _DeleteStartTime;
	}

	public void setToDeleteEndDate() {
		_DeleteEndDate = true;
	}

	public Boolean canDeleteEndDate() {
		return _DeleteEndDate;
	}

	public void setToDeleteEndTime() {
		_DeleteEndTime = true;
	}

	public Boolean canDeleteEndTime() {
		return _DeleteEndTime;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {

	}
}
