package taskie.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
	private String _title;

	// @author A0121555M
	private LocalDate _startDate;
	private LocalDate _endDate;
	private LocalTime _startTime;
	private LocalTime _endTime;

	public Task() {
		_title = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
	}

	// Floating Task (Tasks without specific times)
	public Task(String title) {
		_title = title;
	}
	
	// Deadlines (Done before specific deadline)
	public Task(String title, LocalDate endDate) {
		_title = title;
		_endDate = endDate;
	}
	
	// Deadlines (Done before specific deadline)
	public Task(String title, LocalDate endDate, LocalTime endTime) {
		_title = title;
		_endDate = endDate;
		_endTime = endTime;
	}

	// Timed Task (Specific Start Time and End Time)
	public Task(String title, LocalDate startDate, LocalDate endDate) {
		_title = title;
		_startDate = startDate;
		_endDate = endDate;
	}
		
	// Timed Task (Specific Start Time and End Time)
	public Task(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
		_title = title;
		_startDate = startDate;
		_startTime = startTime;
		_endDate = endDate;
		_endTime = endTime;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String _title) {
		this._title = _title;
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this._startDate = startDate;
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void set_endDate(LocalDate endDate) {
		this._endDate = endDate;
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this._startTime = startTime;
	}

	public LocalTime getEndTime() {
		return _endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this._endTime = endTime;
	}

}
