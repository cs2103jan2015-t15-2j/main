/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N
 

package taskie.commands;

import java.util.Calendar;

import taskie.models.CommandType;
import taskie.models.Task;

public class DeleteCommand implements ICommand {
	private String _taskName;
	private Calendar _startTimeToDelete;
	private Calendar _endTimeToDelete;
	private CommandType _commandType = CommandType.DELETE;

	public DeleteCommand() {
		_taskName = null;
		_startTimeToDelete = null;
		_endTimeToDelete = null;
	}
	
	public DeleteCommand(Task task) {
		
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

	public void setStartTimeToDelete(Calendar startTime) {
		_startTimeToDelete = startTime;
	}

	public void setEndTimeToDelete(Calendar endTime) {
		_endTimeToDelete = endTime;
	}

	public Calendar getStartTimeToDelete() {
		return _startTimeToDelete;
	}

	public Calendar getEndTimeToDelete() {
		return _endTimeToDelete;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
