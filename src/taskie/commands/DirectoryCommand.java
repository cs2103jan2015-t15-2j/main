//@author A0121555M
package taskie.commands;

import java.io.IOException;

import taskie.exceptions.ConfigurationFailedException;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Messages;

public class DirectoryCommand extends AbstractCommand {
	private CommandType _commandType = CommandType.DIRECTORY;
	private String _path;
	
	public DirectoryCommand() {
		_path = "";
	}
	
	public DirectoryCommand(String path) {
		this();
		_path = path;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		try {
			String folder;
			if ( _path.equals("") ) {
				folder = _controller.getUI().loadSelectDirectoryDialog(_controller.getStorage().getStorageLocation());
			} else {
				folder = _path;
			}
			String currentFolder = _controller.getStorage().getStorageLocation();

			if (folder != null && folder.equals(currentFolder)) {
				_controller.getStorage().setStorageLocation(folder);
				_controller.getConfiguration().setDatabasePath(folder);
				_controller.getUI().display(String.format(Messages.DIRECTORY_CHANGED_STRING, folder));
			}
		} catch (NullPointerException e) {
			// Directory Change Cancelled
		} catch (ConfigurationFailedException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
