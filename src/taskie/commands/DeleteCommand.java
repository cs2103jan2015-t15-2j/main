/**
 * class representing an delete command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;

public class DeleteCommand extends AbstractCommand {	
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
	
	//@author A0121555M	
	public DeleteCommand(Task task) {
		_task = task;
		_taskName = task.getTitle();
		_deleteStartDate = false;
		_deleteStartTime = false;
		_deleteEndDate = false;
		_deleteEndTime = false;
	}

	//@author A0097582N
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
			if ( _taskName == null ) {
				getTaskFromUI();
			}
			
			if (canDeleteStartDate() || canDeleteStartTime() || canDeleteEndDate()
					|| canDeleteEndTime()) {	//if either of these methods returned true, only task fields are to be deleted.
				deleteTaskField();
				_controller.getUI().display(DisplayType.SUCCESS, formatDeleteTaskFieldString());
			}else{
				deleteTask();
				_controller.getUI().display(DisplayType.SUCCESS, formatDeleteTaskString());
			}
		} catch (InvalidTaskException e) {
			_controller.getUI().display(DisplayType.ERROR, Messages.INVALID_TASK_NUM);
		} catch (TaskTypeNotSupportedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskModificationFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		} catch (TaskRetrievalFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, e.getMessage());
		}
	}

	
	private void getTaskFromUI() throws InvalidTaskException, TaskRetrievalFailedException {
		if(_taskIndex==0){
			_task = _controller.getLastTask();
			_taskName = _task.getTitle();
		}else{
			_task = _controller.getUI().getTask(_taskIndex);
			_taskName=_task.getTitle();
		}
	}

	private void deleteTaskField() {
		//taskfields are deleted by setting to them null;
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
		_controller.executeCommand(updateCommand);
	}
	
	
	//@author A0121555M
	private void deleteTask() throws TaskTypeNotSupportedException, TaskModificationFailedException {
		_controller.getStorage().deleteTask(_task);
	}
	
	//@author A0097582N
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
	public void undo() {
		new AddCommand(_task).execute();
	}
	
	@Override	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex);
		return sb.toString();
	}
}
