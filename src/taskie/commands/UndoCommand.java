

//@author A0121555M

 

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
// @author       A0097582N
	@Override
	public CommandType getCommandType() {
		return _commandType;
	}
}
