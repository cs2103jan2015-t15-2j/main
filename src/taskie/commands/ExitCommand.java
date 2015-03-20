//@author A0121555M
package taskie.commands;

import taskie.Taskie;
import taskie.models.CommandType;

public class ExitCommand implements ICommand {
	private CommandType _commandType = CommandType.EXIT;

	public ExitCommand() {
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		Taskie.Controller.getUI().display(taskie.models.Messages.EXIT_MESSAGE);
		Taskie.Controller.getUI().exit();
	}
	
	//@author A0121555M
	@Override	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType);
		return sb.toString();
	}
}
