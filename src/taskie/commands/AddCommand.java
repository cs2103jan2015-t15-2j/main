package taskie.commands;

/**
 * class representing an add command.
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

import java.time.LocalDate;
import java.time.LocalTime;

import taskie.models.CommandType;
import taskie.models.Task;

public class AddCommand implements ICommand {
	private String _taskname;
	private LocalDate _startDate;
	private LocalTime _startTime;
	private LocalDate _endDate;
	private LocalTime _endTime;
	private CommandType _commandType = CommandType.ADD;

	public AddCommand() {
		_taskname = null;
		_startDate = null;
		_startTime = null;
		_endDate = null;
		_endTime = null;
	}

	public String get_taskname() {
		return _taskname;
	}

	public void set_taskname(String _taskname) {
		this._taskname = _taskname;
	}

	public LocalDate get_startDate() {
		return _startDate;
	}

	public void set_startDate(LocalDate _startDate) {
		this._startDate = _startDate;
	}

	public LocalTime get_startTime() {
		return _startTime;
	}

	public void set_startTime(LocalTime _startTime) {
		this._startTime = _startTime;
	}

	public LocalDate get_endDate() {
		return _endDate;
	}

	public void set_endDate(LocalDate _endDate) {
		this._endDate = _endDate;
	}

	public LocalTime get_endTime() {
		return _endTime;
	}

	public void set_endTime(LocalTime _endTime) {
		this._endTime = _endTime;
	}

	public CommandType getCommandType() {
		return _commandType;
	}
}
