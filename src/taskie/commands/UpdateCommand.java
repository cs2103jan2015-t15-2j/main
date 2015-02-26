/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.commands;

import java.time.LocalDate;
import java.time.LocalTime;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;

public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE = 2;
	private int _taskIndex;
	private LocalDate _startDateToUpdate;
	private LocalTime _startTimeToUpdate;
	private LocalDate _endDateToUpdate;
	private LocalTime _endTimeToUpdate;

	private Boolean _isToUpdateStartDate = false;
	private Boolean _isToUpdateStartTime = false;
	private Boolean _isToUpdateEndDate = false;
	private Boolean _isToUpdateEndTime = false;
	private CommandType _commandType = CommandType.UPDATE;

	public UpdateCommand() {

		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;

	}

	public UpdateCommand(int taskIndex) {
		_taskIndex = taskIndex;
		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public int getTaskIndex() {
		return _taskIndex;
	}

	public void setTaskIndex(int taskIndex) {
		this._taskIndex = taskIndex;
	}

	public LocalDate getStartDateToUpdate() {
		return _startDateToUpdate;
	}

	public void setStartDateToUpdate(LocalDate startDateToUpdate) {
		_isToUpdateStartDate=true;
		this._startDateToUpdate = startDateToUpdate;
	}

	public LocalTime getStartTimeToUpdate() {
		return _startTimeToUpdate;
	}

	public void setStartTimeToUpdate(LocalTime startTimeToUpdate) {
		_isToUpdateStartTime=true;
		this._startTimeToUpdate = startTimeToUpdate;
	}

	public LocalDate getEndDateToUpdate() {
		return _endDateToUpdate;
	}

	public void setEndDateToUpdate(LocalDate endDateToUpdate) {
		_isToUpdateEndDate=true;
		this._endDateToUpdate = endDateToUpdate;
	}

	public LocalTime getEndTimeToUpdate() {
		return _endTimeToUpdate;
	}

	public void setEndTimeToUpdate(LocalTime endTimeToUpdate) {
		_isToUpdateEndTime=true;
		this._endTimeToUpdate = endTimeToUpdate;
	}

	
	public Boolean isModifedStartDate(){
		return _isToUpdateStartDate;
	}
	
	public Boolean isModifedStartTime(){
		return _isToUpdateStartTime;
	}
	
	public Boolean getIsModifedEndDate(){
		return _isToUpdateEndDate;
	}
	
	public Boolean getIsModifedEndTime(){
		return _isToUpdateEndTime;
	}
	
	@Override
	public void execute() {
		Task task = retrieveTaskToUpdate();
		task = updateTask(task);
		if(Taskie.Storage!=null){
			Taskie.Storage.storeUpdatedTask(task);
		}
		Taskie.UI.display(formatUpdateMsg(task));

	}

	private String formatUpdateMsg(Task task) {
		
		return String.format("STUB MSG_Update Task Title:%s Task Time:%s",
				task.getTitle(),
				Taskie.Controller.formatTime(task.getStartDate(), task.getStartTime()));
	}

	private Task retrieveTaskToUpdate() {
		Task[] taskList=Taskie.UI.getCurrentTaskList();
		Task task = taskList[_taskIndex];
		return task;
	}

	private Task updateTask(Task task) {
		if(_isToUpdateStartDate==true){
			task.setStartDate(_startDateToUpdate);
		}
		if(_isToUpdateStartTime==true){
			task.setStartTime(_startTimeToUpdate);
		}
		if(_isToUpdateEndDate==true){
			task.setEndDate(_endDateToUpdate);
		}
		if(_isToUpdateEndTime==true){
			task.setEndTime(_endTimeToUpdate);
		}
		
		return task;
	}

}
