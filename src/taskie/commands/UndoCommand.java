package taskie.commands;

import taskie.models.CommandType;

public class UndoCommand implements ICommand {
	private CommandType _commandType = CommandType.UNDO;
	
	public UndoCommand(){
		
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}
}
