/**
 * class representing a readcommand.
 * Still under development
 * Bugs: none known
 *
 */ 
//@author A0097582N -unused
//functionality realised by viewCommand
 

package taskie.commands;

import java.util.Calendar;

import taskie.models.CommandType;

public class ReadCommand implements ICommand {
	private Calendar _readDate;
	private CommandType _commandType = CommandType.VIEW;
	
	public ReadCommand(){
		_readDate=null;
	}
	
	public ReadCommand(Calendar readDate){
		_readDate=readDate;
	}
	
	public void setReadDate(Calendar readDate){
		_readDate=readDate;
	}
	
	//return null if no read date is specified.
	public Calendar getReadDate(){
		return _readDate;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
}
