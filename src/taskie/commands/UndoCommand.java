//@author A0121555M
package taskie.commands;

import java.util.Stack;

import taskie.Taskie;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Messages;

public class UndoCommand implements ICommand {
	private CommandType _commandType = CommandType.UNDO;
	private int _steps;
	
	public UndoCommand() {
		this._steps = 1;
	}
	
	public UndoCommand(int steps) {
		this._steps = steps;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		Stack<ICommand> undoStack = Taskie.Controller.getUndoStack();
		Stack<ICommand> redoStack = Taskie.Controller.getRedoStack();
		
		if ( undoStack.size() > 0 ) {
			for ( int x = 0; x < _steps; x++ ) {
				try {
					ICommand cmd = undoStack.pop();
					cmd.undo();
					redoStack.add(cmd);
				} catch (UndoNotSupportedException e) {
					Taskie.Controller.getUI().display(e.getMessage());
				}
			}
		} else {
			Taskie.Controller.getUI().display(Messages.NOTHING_TO_UNDO);
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
