
//@author A0121555M
/**
 * class representing a undo command. 
 * Still under development
 * Bugs: none known
 *
 */
// @author       A0097582N
 

package taskie.commands;

import taskie.models.CommandType;

public class UndoCommand implements ICommand {
	private CommandType _commandType = CommandType.UNDO;
	private int _steps;
	
	public UndoCommand() {
		this._steps = 1;
	}
	
	public UndoCommand(int steps) {
		this._steps = steps;
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}
}
