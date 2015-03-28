//@author A0121555M
package taskie.commands;

import java.io.IOException;

import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;

public class ExitCommand extends AbstractCommand {
	private CommandType _commandType = CommandType.EXIT;

	public ExitCommand() {
		super();
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		try {
			_controller.getStorage().close();
		} catch (IOException e) {}

		_controller.getUI().display(taskie.models.Messages.EXIT_MESSAGE);
		_controller.getUI().exit();
	}
	
	public void undo() throws UndoNotSupportedException {
		throw new UndoNotSupportedException();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType);
		return sb.toString();
	}
}
