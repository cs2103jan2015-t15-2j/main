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
import java.util.ArrayList;
import java.util.HashMap;

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
	
	public Boolean isModifedEndDate(){
		return _isToUpdateEndDate;
	}
	
	public Boolean isModifedEndTime(){
		return _isToUpdateEndTime;
	}
	
	@Override
	public void execute() {
		Task task = retrieveTaskToUpdateFromParser();
		retrieveTaskToUpdateFromStorageAndUpdate(task);
		Taskie.UI.display(formatUpdateMsg(task));
		//TODO add message return to user
	}

	private Task updateTask(Task task) {
		Task updatedTask = new Task(task.getTitle());
		if(this.isModifedStartDate()){
			updatedTask.setStartDate(this.getStartDateToUpdate());
		}
		if(this.isModifedStartTime()){
			updatedTask.setStartTime(this.getStartTimeToUpdate());
		}
		if(this.isModifedEndDate()){
			updatedTask.setEndDate(this.getEndDateToUpdate());
		}
		if(this.isModifedEndTime()){
			updatedTask.setEndTime(this.getEndTimeToUpdate());
		}
		return updatedTask;
	}

	private void retrieveTaskToUpdateFromStorageAndUpdate(Task task) {
		HashMap<String, ArrayList<Task>> taskLists=Taskie.Storage.retrieveTaskMap();
		String taskType = Taskie.Controller.determineTaskType(task);
		ArrayList<Task> taskList= taskLists.get(taskType);
		int taskIndex= taskList.indexOf(task);
		Task updatedTask= updateTask(taskList.get(taskIndex));
		if(taskType.equals(Taskie.Controller.determineTaskType(task))){
			taskList.remove(taskIndex);
			taskList.add(taskIndex, updatedTask);
		}else{
			taskList.remove(taskIndex);
			taskLists.get(Taskie.Controller.determineTaskType(updatedTask)).add(updatedTask);
		}
		Taskie.Storage.storeTaskMap(taskLists);
	}



	private String formatUpdateMsg(Task task) {
		
		return String.format("STUB MSG_Update Task Title:%s Task Time:%s",
				task.getTitle(),
				Taskie.Controller.formatTime(task.getStartDate(), task.getStartTime()));
	}

	private Task retrieveTaskToUpdateFromParser() {
		Task[] taskList=Taskie.UI.getCurrentTaskList();
		Task task = taskList[_taskIndex];
		return task;
	}


}
