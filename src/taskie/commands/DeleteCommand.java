/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;

public class DeleteCommand implements ICommand {
	
	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";
	
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
		Task[] tasks = Taskie.Controller.getUI().getCurrentTaskList();
		_task = tasks[taskId];
		_taskName = _task.getTitle();
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
		if (canDeleteStartDate() && canDeleteStartTime() && canDeleteEndDate()
				&& canDeleteEndTime()) {
			deleteTask();
		}else{
			deleteTaskField();
		}
		//TODO return message to user
	}

	private void deleteTaskField() {
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
		String taskType=Taskie.Controller.determineTaskType(_task);
		if(taskType.equals(FLOATING_TASKNAME)){
			Taskie.Controller.getStorage().deleteFloatingTask(_task);
		}else if(taskType.equals(DEADLINED_TASKNAME)){
			Taskie.Controller.getStorage().deleteDeadlinedTask(_task);
		}else{
			Taskie.Controller.getStorage().deleteTimedTask(_task);
		}
		
	}
}
