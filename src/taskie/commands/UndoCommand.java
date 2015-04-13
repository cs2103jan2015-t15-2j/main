//@author A0121555M
package taskie.commands;

import java.util.Stack;

import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;

public class UndoCommand extends AbstractCommand {
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

	public boolean execute() {
		boolean success = true;
		Stack<ICommand> undoStack = _controller.getUndoStack();
		Stack<ICommand> redoStack = _controller.getRedoStack();
		
		if ( undoStack.size() > 0 ) {
			int count = Math.min(_steps, undoStack.size());
			for ( int x = 0; x < count; x++ ) {
				try {
					ICommand cmd = undoStack.pop();
					cmd.undo();
					redoStack.add(cmd);
				} catch (UndoNotSupportedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
					success = false;
				}
			}
		} else {
			_controller.getUI().display(DisplayType.ERROR, Messages.NOTHING_TO_UNDO);
			success = false;
		}
		
		return success;
	}
	
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
