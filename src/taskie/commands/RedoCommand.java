//@author A0121555M
package taskie.commands;

import java.util.Stack;

import taskie.Taskie;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Messages;

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
		Stack<ICommand> undoStack = Taskie.Controller.getUndoStack();
		Stack<ICommand> redoStack = Taskie.Controller.getRedoStack();
		
		if ( redoStack.size() > 0 ) {
			for ( int x = 0; x < _steps; x++ ) {
				ICommand cmd = redoStack.pop();
				cmd.execute();
				undoStack.add(cmd);
			}
		} else {
			Taskie.Controller.getUI().display(Messages.NOTHING_TO_REDO);
		}
	}
	
	//@author A0121555M
	public void undo() throws UndoNotSupportedException {
		throw new UndoNotSupportedException();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("Steps:" + _steps);
		return sb.toString();
	}
}
