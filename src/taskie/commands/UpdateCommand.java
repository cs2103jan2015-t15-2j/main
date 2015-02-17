package taskie.commands;

import java.util.Calendar;


public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE=2;
	
	private String _taskName;
	private Calendar _startTimeToUpdate;
	private Calendar _endTimeToUpdate;
	
	public UpdateCommand(){
		_taskName=null;
		_startTimeToUpdate=null;
		_endTimeToUpdate=null;
	}
	
	public UpdateCommand(String taskName){
		_taskName = taskName;
		_startTimeToUpdate=null;
		_endTimeToUpdate=null;
	}
	
	public String getTaskName(){
		return _taskName;
	}
	
	public void setTaskName(String taskName){
		_taskName = taskName;
	}
	
	public void setStartTimeToUpdate(Calendar startTime){
		_startTimeToUpdate = startTime;
	}
	
	public void setEndTimeToUpdate(Calendar endTime){
		_endTimeToUpdate = endTime;
	}
	
}