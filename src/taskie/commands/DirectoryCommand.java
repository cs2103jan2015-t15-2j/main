//@author A0121555M
package taskie.commands;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import taskie.exceptions.ConfigurationFailedException;
import taskie.exceptions.FileExistsException;
import taskie.exceptions.StorageLocationInvalidException;
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
			Path currentFolder = _controller.getStorage().getStorageLocation();

			if ( _path.equals("") ) {
				folder = _controller.getUI().loadSelectDirectoryDialog(currentFolder.toString());
			} else {
				folder = _path;
			}
			
			if ( folder == null ) {
				// Directory Change Cancelled
				_controller.getUI().display(String.format(Messages.DIRECTORY_NOT_CHANGED));
			} else {
				if (!folder.equals(currentFolder)) {
					_controller.getStorage().setStorageLocation(FileSystems.getDefault().getPath(folder));
					_controller.getConfiguration().setDatabasePath(folder);
					_controller.getUI().display(String.format(Messages.DIRECTORY_CHANGED, folder));
				}
			}
		} catch (ConfigurationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StorageLocationInvalidException e) {
			_controller.getUI().display(String.format(Messages.DIRECTORY_INVALID));
		} catch (FileExistsException e) {
			_controller.getUI().display(String.format(Messages.DIRECTORY_FILE_EXISTS));
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
