package taskie.models;

import java.util.Calendar;

public class Task {
	private String _title;
	private Calendar _startTime;
	private Calendar _endTime;

	public Task(){
		_title=null;
		_startTime=null;
		_endTime=null;
	}
	
	public Task(String title){
		_title=title;
	}
	
	public Task(String title, Calendar startTime, Calendar endTime){
		_title=title;
		_startTime=startTime;
		_endTime=endTime;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String _title) {
		this._title = _title;
	}
	public Calendar getStartTime() {
		return _startTime;
	}
	public void setStartTime(Calendar _startTime) {
		this._startTime = _startTime;
	}
	public Calendar getEndTime() {
		return _endTime;
	}
	public void setEndTime(Calendar _endTime) {
		this._endTime = _endTime;
	}

}
