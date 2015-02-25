/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 * @author       A0097582N
 */

package taskie.commands;

import java.util.Calendar;
import taskie.models.CommandType;

public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE = 2;

	private String _taskName;
	private Calendar _startTimeToUpdate;
	private Calendar _endTimeToUpdate;
	private CommandType _commandType = CommandType.UPDATE;

	public UpdateCommand() {
		_taskName = null;
		_startTimeToUpdate = null;
		_endTimeToUpdate = null;
	}

	public UpdateCommand(String taskName) {
		_taskName = taskName;
		_startTimeToUpdate = null;
		_endTimeToUpdate = null;
	}

	public String getTaskName() {
		return _taskName;
	}

	public void setTaskName(String taskName) {
		_taskName = taskName;
	}

	public void setStartTimeToUpdate(Calendar startTime) {
		_startTimeToUpdate = startTime;
	}

	public Calendar getStartTimeToUpdate() {
		return _startTimeToUpdate;
	}

	public void setEndTimeToUpdate(Calendar endTime) {
		_endTimeToUpdate = endTime;
	}

	public Calendar getEndTimeToUpdate() {
		return _endTimeToUpdate;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

}
