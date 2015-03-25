//@author A0121555M
package taskie.commands;

import java.io.IOException;

import taskie.Taskie;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;

public class ExitCommand implements ICommand {
	private CommandType _commandType = CommandType.EXIT;

	public ExitCommand() {
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		try {
			Taskie.Controller.getStorage().close();
		} catch (IOException e) {}
		Taskie.Controller.getUI().display(taskie.models.Messages.EXIT_MESSAGE);
		Taskie.Controller.getUI().exit();
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
