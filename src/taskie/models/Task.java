package taskie.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.Serializable;

import taskie.exceptions.TaskDateInvalidException;
import taskie.exceptions.TaskDateNotSetException;

public class Task implements Comparable<Task>, Serializable {
	private String _title;

	//@author A0121555M
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private Boolean _isDone;

	public Task() {
		_title = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
		_isDone = false;
	}

	// Floating Task (Tasks without specific times)
	public Task(String title) {
		_title = title;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
		_isDone = false;
	}

	// Deadlines (Done before specific deadline)
	public Task(String title, LocalDateTime endDateTime) {
		_title = title;
		_startDate = null;
		_startTime = null;
		_endDate = endDateTime.toLocalDate();
		_endTime = endDateTime.toLocalTime();
		_isDone = false;
	}

	public Task(String title, LocalDate endDate) {
		_title = title;
		_startDate = null;
		_startTime = null;
		_endDate = endDate;
		_endTime = null;
		_isDone = false;
	}
	
	public Task(String title, LocalDate endDate, LocalTime endTime) {
		_title = title;
		_startDate = endDate;
		_startTime = endTime;
		_endDate = endDate;
		_endTime = endTime;
		_isDone = false;
	}

	// Timed Task (Specific Start Time and End Time)
	public Task(String title, LocalDateTime startDateTime, LocalDateTime endDateTime) {
		_title = title;
		_startDate = startDateTime.toLocalDate();
		_startTime = startDateTime.toLocalTime();
		_endDate = endDateTime.toLocalDate();
		_endTime = endDateTime.toLocalTime();
		_isDone = false;
	}

	public Task(String title, LocalDate startDate, LocalDate endDate) {
		_title = title;
		_startDate = startDate;
		_startTime = null;
		_endDate = endDate;
		_endTime = null;
		_isDone = false;
	}
	
	public Task(String title, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
		_title = title;
		_startDate = startDate;
		_startTime = startTime;
		_endDate = endDate;
		_endTime = endTime;
		_isDone = false;
	}
	
	//@author A0097582N
	public Task(Task task){
		_title=task.getTitle();
		_startDate= task.getStartDate();
		_startTime = task.getStartTime();
		_endDate = task.getEndDate();
		_endTime= task.getEndTime();
		_isDone = task.isDone();
	}

	//@author A0121555M
	public String getTitle() {
		return _title;
	}

	public void setTitle(String _title) {
		this._title = _title;
	}

	public LocalDateTime getStartDateTime() {
		if (_startDate == null) {
			return null;
		}

		return LocalDateTime.of(_startDate, (_startTime == null) ? LocalTime.MAX : _startTime);
	}

	public void setStartDateTime(LocalDateTime startDateTime) throws TaskDateNotSetException, TaskDateInvalidException {
		LocalDateTime start = startDateTime;
		LocalDateTime end = this.getEndDateTime();
		
		if ( isValidDateTime(start, end) ) {
			_startDate = startDateTime.toLocalDate();
			_startTime = startDateTime.toLocalTime();
		} else {
			throw new TaskDateInvalidException("startDateTime after endDateTime");			
		}
	}

	public LocalDateTime getEndDateTime() {
		if (_endDate == null) {
			return null;
		}

		return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX : _endTime);
	}

	public void setEndDateTime(LocalDateTime endDateTime) throws TaskDateNotSetException, TaskDateInvalidException {
		LocalDateTime start = this.getStartDateTime();
		LocalDateTime end = endDateTime;
		
		if ( isValidDateTime(start, end) ) {
			_endDate = endDateTime.toLocalDate();
			_endTime = endDateTime.toLocalTime();
		} else {
			throw new TaskDateInvalidException("startDateTime after endDateTime");			
		}
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate startDate) throws TaskDateInvalidException {
		LocalDateTime start = this.getStartDateTime().with(startDate);
		LocalDateTime end = this.getEndDateTime();
		
		if (isValidDateTime(start, end) ) {
			_startDate = startDate;
		} else {
			throw new TaskDateInvalidException("startDateTime after endDateTime");			
		}
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void setEndDate(LocalDate endDate) throws TaskDateInvalidException {
		LocalDateTime start = this.getStartDateTime();
		LocalDateTime end = this.getEndDateTime().with(endDate);
		
		if ( isValidDateTime(start, end) ) {
			_endDate = endDate;
		} else {
			throw new TaskDateInvalidException("startDateTime after endDateTime");			
		}
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public void setStartTime(LocalTime startTime) throws TaskDateNotSetException, TaskDateInvalidException {
		if ( _startDate == null ) {
			 throw new TaskDateNotSetException("Start date not set");
		}
		
		LocalDateTime start = this.getStartDateTime().with(startTime);
		LocalDateTime end = this.getEndDateTime();
		
		if ( isValidDateTime(start, end) ) {
			this._startTime = startTime;
		} else {
			throw new TaskDateInvalidException("startDateTime after endDateTime");			
		}
	}

	public LocalTime getEndTime() {
		return _endTime;
	}

	public void setEndTime(LocalTime endTime) throws TaskDateNotSetException, TaskDateInvalidException {
		if ( _endDate == null ) {
			 throw new TaskDateNotSetException("End date not set");
		}
		
		LocalDateTime start = this.getStartDateTime();
		LocalDateTime end = this.getEndDateTime().with(endTime);
		
		if ( isValidDateTime(start, end) ) {
			this._endTime = endTime;
		} else {
			throw new TaskDateInvalidException("endDateTime before startDateTime");			
		}
	}

	public void setTaskDone() {
		_isDone = true;
	}

	public void setTaskUndone() {
		_isDone = false;
	}

	public Boolean isDone() {
		return _isDone;
	}

	//@author A0121555M
	public TaskType getTaskType() {
		if (_startDate == null && _endDate == null) {
			return TaskType.FLOATING;
		} else if (_startDate == null && _endDate != null) {
			return TaskType.DEADLINE;
		} else {
			return TaskType.TIMED;
		}
	}
	
	private boolean isValidDateTime(LocalDateTime start, LocalDateTime end) {
		if ( start == null ) {
			// user can set end only but no start (deadlined)
			return true;
		}
		
		if ( start.isAfter(end) || end.isBefore(start)) {
			return false;
		}

		return true;
	}

	// @author A0097582N
	//this constructor is used to duplicate a task.
	public int compareTo(Task other) {
		LocalDateTime thisTaskDateTime = LocalDateTime.of(this.getEndDate(),
				this.getEndTime());
		LocalDateTime otherTaskDateTime = LocalDateTime.of(other.getEndDate(),
				other.getEndTime());
		return thisTaskDateTime.compareTo(otherTaskDateTime);
	}

	public int compareTo(LocalDateTime now) {
		LocalDateTime thisTaskDateTime = LocalDateTime.of(this.getEndDate(),
				this.getEndTime());
		return thisTaskDateTime.compareTo(now);
	}

	//@author A0135137L-unused
	//Reason for unused: Redundant, see getTaskType() - Yunheng
	/*
	public boolean isDeadlined() {
		if (_title != null && _startDate == null && _startTime == null
				&& _endDate != null)
			return true;
		else
			return false;
	}

	public boolean isTimed() {
		if (_title != null && _startDate != null && _endDate != null)
			return true;
		else
			return false;
	}
	*/
	
	//@author A0135137L
	public boolean equals(Task other) {
		if (equalsTitle(other) && equalsStartDate(other)
				&& equalsStartTime(other) && equalsEndDate(other)
				&& equalsEndTime(other)) {
			return true;
		}
		return false;
	}

	private boolean equalsTitle(Task other) {
		return _title.equals(other.getTitle());
	}

	private boolean equalsStartDate(Task other) {
		LocalDate otherStartDate = other.getStartDate();
		if (_startDate == null && otherStartDate == null)
			return true;
		if (_startDate != null && otherStartDate != null) {
			return _startDate.equals(otherStartDate);
		}
		return false;
	}

	private boolean equalsEndDate(Task other) {
		LocalDate otherEndDate = other.getEndDate();
		if (_endDate == null && otherEndDate == null)
			return true;
		if (_endDate != null && otherEndDate != null) {
			return _endDate.equals(otherEndDate);
		}
		return false;
	}

	private boolean equalsStartTime(Task other) {
		LocalTime otherStartTime = other.getStartTime();
		if (_startTime == null && otherStartTime == null)
			return true;
		if (_startTime != null && otherStartTime != null) {
			return _startTime.equals(otherStartTime);
		}
		return false;
	}

	private boolean equalsEndTime(Task other) {
		LocalTime otherEndTime = other.getEndTime();
		if (_endTime == null && otherEndTime == null)
			return true;
		if (_endTime != null && otherEndTime != null) {
			return _endTime.equals(otherEndTime);
		}
		return false;
	}
	
	//A0135137L
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("TaskName:" + this.getTitle() + ",");
		try {
			sb.append("StartDate:" + this.getStartDate() + ",");
		} catch (NullPointerException e) {
			sb.append("StartDate:null,");
		}
		try {
			sb.append("StartDateTime:" + this.getStartDateTime() + ",");
		} catch (NullPointerException e) {
			sb.append("StartDateTime:null,");
		}
		try {
			sb.append("EndDate:" + this.getEndDate());
		} catch (NullPointerException e) {
			sb.append("EndDate:null");
		}
		try {
			sb.append("EndDateTime:" + this.getEndDateTime());
		} catch (NullPointerException e) {
			sb.append("EndDateTime:null");
		}

		return sb.toString();
	}
}