package taskie.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import taskie.models.CommandType;
import taskie.models.ViewType;

public class ViewCommand implements ICommand {
	private CommandType _commandType = CommandType.VIEW;
	
	//@author A0121555M
	private ViewType _viewType;
	private LocalDate _startDate;
	private LocalDate _endDate;
	private LocalTime _startTime;
	private LocalTime _endTime;
	private String _searchKeywords;

	public ViewCommand(ViewType viewType) {
		_viewType = viewType;
	}

	public LocalDateTime getStartDateTime() {
		return LocalDateTime.of(_startDate, (_startTime == null) ? LocalTime.MAX : _startTime);
	}
	
	public void setStartDateTime(LocalDateTime startDateTime) {
		this.setStartDate(startDateTime.toLocalDate());
		this.setStartTime(startDateTime.toLocalTime());
	}

	public LocalDateTime getEndDateTime() {
		return LocalDateTime.of(_endDate, (_endTime == null) ? LocalTime.MAX : _endTime);
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.setEndDate(endDateTime.toLocalDate());
		this.setEndTime(endDateTime.toLocalTime());
	}

	public LocalDate getStartDate() {
		return _startDate;
	}

	public void setStartDate(LocalDate startDate) {
		if ( _endDate == null ) {
			this._endDate = startDate;
		}
		
		if ( _endDate.isBefore(startDate) ) {
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
		if ( _startDate == null ) {
			this._startDate = endDate;
		}
		
		if ( _startDate.isAfter(endDate) ) {
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

	public String getSearchKeywords() {
		return _searchKeywords;
	}

	public void setSearchKeywords(String searchKeywords) {
		this._searchKeywords = searchKeywords;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
