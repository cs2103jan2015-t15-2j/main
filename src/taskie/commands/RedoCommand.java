//@author A0121555M
package taskie.commands;

import taskie.models.CommandType;

public class RedoCommand implements ICommand {
	private CommandType _commandType = CommandType.REDO;
	private int _steps;
	
	public RedoCommand() {
		this._steps = 1;
	}
	
	public RedoCommand(int steps) {
		this._steps = steps;
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
