package taskie.commands;

import java.util.Calendar;

public class DeleteCommand implements ICommand {
	private String _taskName;
	private Calendar _startTimeToDelete;
	private Calendar _endTimeToDelete;
	
	public DeleteCommand(){
		_taskName=null;
		_startTimeToDelete=null;
		_endTimeToDelete=null;
	}
	
	public DeleteCommand(String taskName){
		_taskName = taskName;
	}
	
	public void setTaskName(String taskName){
		_taskName=taskName;
	}
	
	public String getTaskName(){
		return _taskName;
	}
	
	public void setStartTimeToDelete(Calendar startTime){
		_startTimeToDelete = startTime;
	}
	
	public void setEndTimeToDelete(Calendar endTime){
		_endTimeToDelete = endTime;
	}
	
	public Calendar getStartTimeToDelete(){
		return _startTimeToDelete;
	}
	
	public Calendar getEndTimeToDelete(){
		return _endTimeToDelete;
	}
}
