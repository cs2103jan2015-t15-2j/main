package taskie.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.Serializable;

public class Task implements Comparable<Task>, Serializable {
	private String _title;

	//@author A0121555M
	private LocalDate _startDate;
	private LocalDate _endDate;
	private LocalTime _startTime;
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
		_isDone = false;
	}

	// Deadlines (Done before specific deadline)
	public Task(String title, LocalDate endDate) {
		_title = title;
		_endDate = endDate;
		_isDone = false;
	}

	// Deadlines (Done before specific deadline)
	public Task(String title, LocalDate endDate, LocalTime endTime) {
		_title = title;
		_endDate = endDate;
		_endTime = endTime;
		_isDone = false;
	}

	// Timed Task (Specific Start Time and End Time)
	public Task(String title, LocalDate startDate, LocalDate endDate) {
		_title = title;
		_startDate = startDate;
		_endDate = endDate;
		_isDone = false;
	}

	// Timed Task (Specific Start Time and End Time)
	public Task(String title, LocalDate startDate, LocalTime startTime,
			LocalDate endDate, LocalTime endTime) {
		_title = title;
		_startDate = startDate;
		_startTime = startTime;
		_endDate = endDate;
		_endTime = endTime;
		_isDone = false;
	}

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

		return LocalDateTime.of(_startDate,
				(_startTime == null) ? LocalTime.MAX : _startTime);
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.setStartDate(startDateTime.toLocalDate());
		this.setStartTime(startDateTime.toLocalTime());
	}

	public LocalDateTime getEndDateTime() {
		if (_endDate == null) {
			return null;
		}

		return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX
				: _endTime);
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.setEndDate(endDateTime.toLocalDate());
		this.setEndTime(endDateTime.toLocalTime());
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate startDate) {
		if (_endDate == null) {
			this._endDate = startDate;
		}

		if (_endDate.isBefore(startDate)) {
			this._startDate = this._endDate;
			this._endDate = startDate;
		} else {
			this._startDate = startDate;
		}
	}

	public LocalDate getEndDate() {
		return _endDate;
	}

	public void setEndDate(LocalDate endDate) {
		if (_startDate == null) {
			this._startDate = endDate;
		}

		if (_startDate.isAfter(endDate)) {
			this._endDate = this._startDate;
			this._startDate = endDate;
		} else {
			this._endDate = endDate;
		}
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

	public void setTaskDone() {
		_isDone = true;
	}

	public void setTaskUndone() {
		_isDone = false;
	}

	public Boolean getTaskStatus() {
		return _isDone;
	}

	//@author A0121555M-reused
	public TaskType getTaskType() {
		if (this.getStartDate() == null && this.getEndDate() == null) {
			return TaskType.FLOATING;
		} else if (this.getStartDate() == null && this.getEndDate() != null) {
			return TaskType.DEADLINE;
		} else {
			return TaskType.TIMED;
		}
	}

	//@author A0097582N
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

	//@author A0135137L
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
}