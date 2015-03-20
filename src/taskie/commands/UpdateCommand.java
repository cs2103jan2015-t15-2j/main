/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import taskie.Taskie;
import taskie.exceptions.InvalidTaskException;
import taskie.models.CommandType;
import taskie.models.Task;

public class UpdateCommand implements ICommand {
	
	int NUM_ATTRIBUTE = 2;
	private int _taskIndex;
	private String _taskTitleToUpdate;
	private LocalDate _startDateToUpdate;
	private LocalTime _startTimeToUpdate;
	private LocalDate _endDateToUpdate;
	private LocalTime _endTimeToUpdate;

	private Boolean _isToUpdateTaskTitle = false;
	private Boolean _isToUpdateStartDate = false;
	private Boolean _isToUpdateStartTime = false;
	private Boolean _isToUpdateEndDate = false;
	private Boolean _isToUpdateEndTime = false;
	private CommandType _commandType = CommandType.UPDATE;

	public UpdateCommand() {
		_taskTitleToUpdate = null;
		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;

	}

	public UpdateCommand(int taskIndex) {
		_taskIndex = taskIndex;
		_taskTitleToUpdate = null;
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
	
	public void setTaskTitleToUpdate(String title){
		_isToUpdateTaskTitle=true;
		_taskTitleToUpdate = title;
	}
	
	public String getTaskTitleToUpdate(){
		return _taskTitleToUpdate;
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

	public Boolean isModifiedTaskTitle(){
		return _isToUpdateTaskTitle;
	}
	public Boolean isModifiedStartDate(){
		return _isToUpdateStartDate;
	}
	
	public Boolean isModifiedStartTime(){
		return _isToUpdateStartTime;
	}
	
	public Boolean isModifiedEndDate(){
		return _isToUpdateEndDate;
	}
	
	public Boolean isModifiedEndTime(){
		return _isToUpdateEndTime;
	}
	
	@Override
	public void execute() {
		try{
			Task task = retrieveTaskToUpdateFromParser();
			retrieveTaskToUpdateFromStorageAndUpdate(task);
			Taskie.Controller.getUI().display(formatUpdateMsg(task));
		}catch(InvalidTaskException e){
			Taskie.Controller.getUI().display(taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private Task updateTask(Task task) {
		Task updatedTask = new Task(task.getTitle());
		if(this.isModifiedTaskTitle()){
			updatedTask.setTitle(this.getTaskTitleToUpdate());
		}
		if(this.isModifiedStartDate()){
			updatedTask.setStartDate(this.getStartDateToUpdate());
		}
		if(this.isModifiedStartTime()){
			updatedTask.setStartTime(this.getStartTimeToUpdate());
		}
		if(this.isModifiedEndDate()){
			updatedTask.setEndDate(this.getEndDateToUpdate());
		}
		if(this.isModifiedEndTime()){
			updatedTask.setEndTime(this.getEndTimeToUpdate());
		}
		return updatedTask;
	}

	private void retrieveTaskToUpdateFromStorageAndUpdate(Task task) {
		HashMap<String, ArrayList<Task>> taskLists=Taskie.Controller.getStorage().retrieveTaskMap();
		String taskType = Taskie.Controller.determineTaskType(task);
		ArrayList<Task> taskList= taskLists.get(taskType);
		int taskIndexInStorage= taskList.indexOf(task);
		Task updatedTask= updateTask(taskList.get(taskIndexInStorage));
		if(taskType.equals(Taskie.Controller.determineTaskType(task))){
			taskList.remove(taskIndexInStorage);
			taskList.add(taskIndexInStorage, updatedTask);
		}else{
			taskList.remove(taskIndexInStorage);
			taskLists.get(Taskie.Controller.determineTaskType(updatedTask)).add(updatedTask);
		}
		Taskie.Controller.getStorage().storeTaskMap(taskLists);
	}



	private String formatUpdateMsg(Task task) {
		String message=String.format(taskie.models.Messages.UPDATE_STRING,task.getTitle());
		if(this.isModifiedStartDate()){
			message=message.concat("start date");
		}
		if(this.isModifiedStartTime()){
			message=message.concat("start time");
		}
		if(this.isModifiedEndDate()){
			message=message.concat("end date");
		}
		if(this.isModifiedEndTime()){
			message=message.concat("end time");
		}
		return message;
	
	}

	private Task retrieveTaskToUpdateFromParser() throws InvalidTaskException {
			
			Task task = Taskie.Controller.getUI().getTask(_taskIndex);
			return task;
	}


}
