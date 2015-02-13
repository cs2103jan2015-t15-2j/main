package taskie.models;

import java.util.Calendar;

public class Task {
	private String _title;
	private Calendar _startTime;
	private Calendar _endTime;
	private String _description;
	
	public String get_title() {
		return _title;
	}
	public void set_title(String _title) {
		this._title = _title;
	}
	public Calendar get_startTime() {
		return _startTime;
	}
	public void set_startTime(Calendar _startTime) {
		this._startTime = _startTime;
	}
	public Calendar get_endTime() {
		return _endTime;
	}
	public void set_endTime(Calendar _endTime) {
		this._endTime = _endTime;
	}
	public String get_description() {
		return _description;
	}
	public void set_description(String _description) {
		this._description = _description;
	}
}
