//@author A0121555M
package taskie.commands;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;

public class ExitCommand extends AbstractCommand {
	private Logger _logger;
	private CommandType _commandType = CommandType.EXIT;

	public ExitCommand() {
		super();
		_logger = Logger.getLogger(ExitCommand.class.getName());
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		try {
			_controller.getStorage().close();
		} catch (IOException e) {
			_logger.log(Level.INFO, "Failed to close storage properly: " + e.getMessage());
		
		}

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
