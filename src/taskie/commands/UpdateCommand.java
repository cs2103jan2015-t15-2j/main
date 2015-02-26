/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

import taskie.models.CommandType;

public class UpdateCommand implements ICommand {
	int NUM_ATTRIBUTE = 2;
	private int _TaskIndex;
	private LocalDate _startDateToUpdate;
	private LocalTime _startTimeToUpdate;
	private LocalDate _endDateToUpdate;
	private LocalTime _endTimeToUpdate;
	
	private Boolean _isModifiedStartDate=false;
	private Boolean _isModifiedStartTime=false;
	private Boolean _isModifiedEndDate=false;
	private Boolean _isModifiedEndTime=false;
	private CommandType _commandType = CommandType.UPDATE;

	public UpdateCommand() {
		
		_startDateToUpdate = null;
		_startTimeToUpdate = null;
		_startTimeToUpdate = null;
		_endDateToUpdate = null;
		_endTimeToUpdate = null;

	}

	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
