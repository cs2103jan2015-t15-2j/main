package taskie.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import taskie.exceptions.TaskDateInvalidException;
import taskie.exceptions.TaskDateNotSetException;

public class Task implements Comparable<Task> {
	private static final long serialVersionUID = 2253380958397910210L;
	private String _title;

	//@author A0121555M
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private Boolean _isDone;

	//@author A0121555M
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
		_startDate = null;
		_startTime = null;
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
	public Task(Task task) {
		_title = task.getTitle();
		_startDate = task.getStartDate();
		_startTime = task.getStartTime();
		_endDate = task.getEndDate();
		_endTime = task.getEndTime();
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
		this.setStartDateTime(startDateTime.toLocalDate(), startDateTime.toLocalTime());
	}

	public void setStartDateTime(LocalDate startDate, LocalTime startTime) throws TaskDateNotSetException, TaskDateInvalidException {
		if ( startDate == null && startTime != null ) {
			// Invalid State - Date must be set if time is set
			throw new TaskDateNotSetException();
		}
		
		if ( _startDate == null || startDate == null ) {
			 _startDate = startDate;
		} else {
	        Period p = Period.between(_startDate, startDate);
	        _startDate = startDate;
	        _endDate = _endDate.plus(p);
		}
		
		if ( _startTime == null || startTime == null ) {
			_startTime = startTime;
		} else {
	        long difference = ChronoUnit.NANOS.between(_startTime, startTime);
	        _startTime = startTime;
	        _endTime = _endTime.plusNanos(difference);
		}
		
		/*		
		LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
		LocalDateTime endDateTime = this.getEndDateTime();

		if ( startDateTime.isAfter(endDateTime) ) {
			// Invalid State - Start Date / Time is after End Date / Time
			throw new TaskDateInvalidException();			
		}
		*/
	}

	public LocalDateTime getEndDateTime() {
		if (_endDate == null) {
			return null;
		}

		return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX : _endTime);
	}
	
	public void setEndDateTime(LocalDateTime startDateTime) throws TaskDateNotSetException, TaskDateInvalidException {
		this.setEndDateTime(startDateTime.toLocalDate(), startDateTime.toLocalTime());
	}
	
	public void setEndDateTime(LocalDate endDate, LocalTime endTime) throws TaskDateNotSetException, TaskDateInvalidException {
		if ( endDate == null && endTime != null ) {
			// Invalid State - Date must be set if time is set
			throw new TaskDateNotSetException();
		}
		
		LocalDateTime startDateTime = this.getStartDateTime();
		LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime == null ? LocalTime.MAX : endTime);
		
		if ( startDateTime != null && endDateTime.isBefore(startDateTime) ) {
			// Invalid State - End Date / Time is before Start Date / Time
			throw new TaskDateInvalidException();			
		}
		
		_endDate = endDate;
		_endTime = endTime;
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public LocalTime getEndTime() {
		return _endTime;
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

	//@author A0097582N
	// this constructor is used to duplicate a task.
	public int compareTo(Task other) {
		LocalDateTime thisTaskDateTime = LocalDateTime.of(this.getEndDate(), this.getEndTime());
		LocalDateTime otherTaskDateTime = LocalDateTime.of(other.getEndDate(), other.getEndTime());
		return thisTaskDateTime.compareTo(otherTaskDateTime);
	}

	public int compareTo(LocalDateTime now) {
		LocalDateTime thisTaskDateTime = LocalDateTime.of(this.getEndDate(), this.getEndTime());
		return thisTaskDateTime.compareTo(now);
	}

	//@author A0135137L-unused
	// Reason for unused: Redundant, see getTaskType() - Yunheng
	/*
	 * public boolean isDeadlined() { if (_title != null && _startDate == null
	 * && _startTime == null && _endDate != null) return true; else return
	 * false; }
	 * 
	 * public boolean isTimed() { if (_title != null && _startDate != null &&
	 * _endDate != null) return true; else return false; }
	 */

	//@author A0135137L
	public boolean equals(Task other) {
		if (equalsTitle(other) && equalsStartDate(other) && equalsStartTime(other) && equalsEndDate(other) && equalsEndTime(other)) {
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

	// A0135137L
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("TaskName:" + this.getTitle() + ",");

		try {
			sb.append("StartDateTime:" + this.getStartDateTime() + ",");
		} catch (NullPointerException e) {
			sb.append("StartDateTime:null,");
		}

		try {
			sb.append("EndDateTime:" + this.getEndDateTime());
		} catch (NullPointerException e) {
			sb.append("EndDateTime:null");
		}

		return sb.toString();
	}
}