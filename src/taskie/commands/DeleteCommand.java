/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import taskie.Taskie;
import taskie.exceptions.InvalidTaskException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.TaskType;

public class DeleteCommand implements ICommand {	
	private Task _task;
	private String _taskName;
	private int _taskIndex;
	private Boolean _deleteStartDate;
	private Boolean _deleteStartTime;
	private Boolean _deleteEndDate;
	private Boolean _deleteEndTime;

	private CommandType _commandType = CommandType.DELETE;

	public DeleteCommand() {
		_taskName = null;
		_deleteStartDate = false;
		_deleteStartTime = false;
		_deleteEndDate = false;
		_deleteEndTime = false;

	}

	public DeleteCommand(int taskId) {
			_taskIndex = taskId;
			_deleteStartDate = false;
			_deleteStartTime = false;
			_deleteEndDate = false;
			_deleteEndTime = false;
	}

	public DeleteCommand(String taskName) {
		_taskName = taskName;
		_deleteStartDate = false;
		_deleteStartTime = false;
		_deleteEndDate = false;
		_deleteEndTime = false;
	}

	public void setTaskName(String taskName) {
		_taskName = taskName;
	}

	public String getTaskName() {
		return _taskName;
	}

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
	public void execute() {
		try {
			getTaskFromParser();
			if (canDeleteStartDate() || canDeleteStartTime() || canDeleteEndDate()
					|| canDeleteEndTime()) {	//if either of these methods returned true, only task fields are to be deleted.
				deleteTaskField();
				Taskie.Controller.getUI().display(formatDeleteTaskFieldString());
			}else{
				deleteTask();
				Taskie.Controller.getUI().display(formatDeleteTaskString());
			}
		} catch (InvalidTaskException e) {
			Taskie.Controller.getUI().display(taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private void getTaskFromParser() throws InvalidTaskException {
		_task = Taskie.Controller.getUI().getTask(_taskIndex);
		_taskName=_task.getTitle();
	}

	private void deleteTaskField() {
		//taskfields are deleted by setting to null;
		UpdateCommand updateCommand = new UpdateCommand(_taskIndex);
		if(canDeleteStartDate()){
			updateCommand.setStartDateToUpdate(null);
		}
		if(canDeleteStartTime()){
			updateCommand.setStartTimeToUpdate(null);
		}
		if(canDeleteEndDate()){
			updateCommand.setEndDateToUpdate(null);
		}
		if(canDeleteEndTime()){
			updateCommand.setEndTimeToUpdate(null);
		}
		Taskie.Controller.executeCommand(updateCommand);
	}

	private void deleteTask() {	
		TaskType type = _task.getTaskType();
		if ( type == TaskType.FLOATING ) {
			Taskie.Controller.getStorage().deleteFloatingTask(_task);
		} else if ( type == TaskType.DEADLINE ) {
			Taskie.Controller.getStorage().deleteDeadlinedTask(_task);
		} else {
			Taskie.Controller.getStorage().deleteTimedTask(_task);
		}
	}
	
	private String formatDeleteTaskString(){
		return String.format(taskie.models.Messages.DELETE_TASK,_taskName);
	}
	
	private String formatDeleteTaskFieldString(){
		String message=String.format(taskie.models.Messages.DELETE_TASK_FIELD,_taskName);
		if(canDeleteStartDate()){
			message=message.concat("\nStart date");
		}
		if(canDeleteStartTime()){
			message=message.concat("\nStart time");
		}
		if(canDeleteEndDate()){
			message=message.concat("\nEnd date");
		}
		if(canDeleteEndTime()){
			message=message.concat("\nEnd time");
		}
		return message;
	}
	
	//@author A0121555M
	@Override	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex);
		return sb.toString();
	}
}
