//@author A0121555M
package taskie.commands;

import taskie.Taskie;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Messages;

public class DirectoryCommand extends AbstractCommand {
	private CommandType _commandType = CommandType.DIRECTORY;

	public DirectoryCommand() {
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		try {
			String folder = _controller.getUI().loadSelectDirectoryDialog(_controller.getStorage().getStorageLocation());
			String currentFolder = _controller.getStorage().getStorageLocation();
			
			if ( folder != null && folder.equals(currentFolder) ) {
				_controller.getStorage().setStorageLocation(folder);
				_controller.getUI().display(String.format(Messages.DIRECTORY_CHANGED_STRING, folder));
			}
		} catch ( NullPointerException e ) {
			// Directory Change Cancelled
		}
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
