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

	//@author A0097582N
	// staging variables so that task model can be checked for consistency
	// before actual updates.
	private LocalDate _stageStartDate;
	private LocalTime _stageStartTime;
	private LocalDate _stageEndDate;
	private LocalTime _stageEndTime;

	private boolean _isStagedStartDate = false;
	private boolean _isStagedStartTime = false;
	private boolean _isStagedEndDate = false;
	private boolean _isStagedEndTime = false;

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
		this.setStartDate(startDateTime.toLocalDate());
		this.setStartTime(startDateTime.toLocalTime());
	}

	public LocalDateTime getEndDateTime() {
		if (_endDate == null) {
			return null;
		}

		return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX : _endTime);
	}

	public void setEndDateTime(LocalDateTime endDateTime) throws TaskDateNotSetException, TaskDateInvalidException {
		if (this.getStartDateTime() != null && this.getEndDateTime() != null && endDateTime.isBefore(this.getStartDateTime())) {
			throw new TaskDateInvalidException("New End Date Time is after Start Date Time");
		}

		this.setEndDate(endDateTime.toLocalDate());
		this.setEndTime(endDateTime.toLocalTime());
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate newStartDate) throws TaskDateInvalidException {
		if (_startDate == null) {
			_startDate = newStartDate;
		} else {
			Period p = Period.between(_startDate, newStartDate);
			_startDate = newStartDate;
			_endDate = _endDate.plus(p);
		}
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void setEndDate(LocalDate newEndDate) throws TaskDateInvalidException {
		if (_endDate != null && _startDate != null && newEndDate.isBefore(_startDate)) {
			throw new TaskDateInvalidException("New End Date is before Start Date");
		}

		_endDate = newEndDate;
	}

	public LocalTime getStartTime() {
		return _startTime;
	}

	public void setStartTime(LocalTime newStartTime) throws TaskDateInvalidException, TaskDateNotSetException {
		if (newStartTime == null) {
			_startTime = null;
		} else if (_startTime == null) {
			_startTime = newStartTime;
		} else {
			long difference = ChronoUnit.NANOS.between(_startTime, newStartTime);
			_startTime = newStartTime;
			_endTime = _endTime.plusNanos(difference);
		}
	}

	public LocalTime getEndTime() {
		return _endTime;
	}

	public void setEndTime(LocalTime newEndTime) throws TaskDateInvalidException, TaskDateNotSetException {

		if (newEndTime == null) {
			_endTime = null;
		} else {
			LocalDateTime newEndDateTime = LocalDateTime.of(_endDate, newEndTime);
			if (this.getStartDateTime() != null && newEndDateTime.isBefore(this.getStartDateTime())) {
				throw new TaskDateInvalidException("New End Time is after Start Time");
			}
			_endTime = newEndTime;
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

	//@author A0097582N
	public void stageUpdateStartDate(LocalDate startDate) {
		_stageStartDate = startDate;
		_isStagedStartDate = true;
	}

	public void stageUpdateStartTime(LocalTime startTime) {
		_stageStartTime = startTime;
		_isStagedStartTime = true;
	}

	public void stageUpdateEndDate(LocalDate endDate) {
		_stageEndDate = endDate;
		_isStagedEndDate = true;
	}

	public void stageUpdateEndTime(LocalTime endTime) {
		_stageEndTime = endTime;
		_isStagedEndTime = true;
	}

	public void stageUpdateStartDateTime(LocalDate startDate, LocalTime startTime) {
		stageUpdateStartDate(startDate);
		stageUpdateStartTime(startTime);
	}

	public void stageUpdateEndDateTime(LocalDate startDate, LocalTime startTime) {
		stageUpdateStartDate(startDate);
		stageUpdateStartTime(startTime);
	}

	public void pushStageToActual() throws TaskDateInvalidException, TaskDateNotSetException {
		if (isStagingConsistent()) {
			if (_isStagedStartDate) {
				setStartDate(_stageStartDate);
			}
			if (_isStagedStartTime) {
				setStartTime(_stageStartTime);
			}
			if (_isStagedEndDate) {
				setEndDate(_stageEndDate);
			}
			if (_isStagedEndTime) {
				setEndTime(_stageEndTime);
			}
			resetStaging();
		} else {
			resetStaging();
			throw new TaskDateInvalidException();
		}
	}

	public void initialiseStaging() {
		_stageStartDate = _startDate;
		_stageStartTime = _startTime;
		_stageEndDate = _endDate;
		_stageEndTime = _endTime;

	}

	private boolean isStagingConsistent() {
		LocalDateTime stageStartDateTime = createDateTime(_stageStartDate, _stageStartTime);
		LocalDateTime stageEndDateTime = createDateTime(_stageEndDate, _stageEndTime);
		if (_stageStartDate == null && _stageStartTime != null) {
			return false;
		}

		if (_stageEndDate == null && _stageEndTime != null) {
			return false;
		}
		if (stageStartDateTime != null && stageEndDateTime == null) {
			return false;
		}

		if (stageStartDateTime != null && stageEndDateTime != null && stageStartDateTime.isAfter(stageEndDateTime)) {
			return false;
		}
		return true;
	}

	private LocalDateTime createDateTime(LocalDate date, LocalTime time) {
		if (date == null) {
			return null;
		} else {
			if (time == null) {
				return LocalDateTime.of(date, LocalTime.MAX);
			} else {
				return LocalDateTime.of(date, time);
			}
		}
	}

	private void resetStaging() {
		_stageStartDate = null;
		_stageStartTime = null;
		_stageEndDate = null;
		_stageEndTime = null;
		_isStagedStartDate = false;
		_isStagedStartTime = false;
		_isStagedEndDate = false;
		_isStagedEndTime = false;
	}
}