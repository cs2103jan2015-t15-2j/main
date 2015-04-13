//@author A0121555M
package taskie.commands;

import java.util.Stack;

import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;

public class RedoCommand extends AbstractCommand {
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
		Stack<ICommand> undoStack = _controller.getUndoStack();
		Stack<ICommand> redoStack = _controller.getRedoStack();

		if (redoStack.size() > 0) {
			int count = Math.max(_steps, redoStack.size());
			for (int x = 0; x < count; x++) {
				ICommand cmd = redoStack.pop();
				cmd.execute();
				undoStack.add(cmd);
			}
		} else {
			_controller.getUI().display(DisplayType.ERROR, Messages.NOTHING_TO_REDO);
		}
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
