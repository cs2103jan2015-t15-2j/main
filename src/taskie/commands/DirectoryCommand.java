//@author A0121555M
package taskie.commands;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.ConfigurationFailedException;
import taskie.exceptions.FileExistsException;
import taskie.exceptions.StorageLocationInvalidException;
import taskie.exceptions.StorageMigrationFailedException;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;

public class DirectoryCommand extends AbstractCommand {
	private Logger _logger;
	private CommandType _commandType = CommandType.DIRECTORY;
	private String _path;
	private boolean _overwrite;
	
	public DirectoryCommand() {
		_logger = Logger.getLogger(DirectoryCommand.class.getName());
		_path = "";
		_overwrite = false;
	}
	
	public DirectoryCommand(String path, boolean overwrite) {
		this();
		_path = path;
		_overwrite = overwrite;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public void execute() {
		try {
			String folder;
			Path currentFolder = _controller.getStorage().getStorageLocation();

			if ( _path.isEmpty() ) {
				folder = _controller.getUI().loadSelectDirectoryDialog(currentFolder.toString());
			} else {
				folder = _path;
			}
			
			if ( folder == null ) {
				// Directory Change Cancelled
				_controller.getUI().display(DisplayType.DEFAULT, String.format(Messages.DIRECTORY_NOT_CHANGED));
			} else {
				if (!folder.equals(currentFolder)) {
					try {
						_controller.getStorage().setStorageLocation(FileSystems.getDefault().getPath(folder), _overwrite);
					} catch ( FileExistsException e ) {
						_logger.log(Level.INFO, String.format("Database exists at %s", currentFolder));
					}
					
					_controller.getConfiguration().setDatabasePath(folder);
					_controller.getUI().display(DisplayType.SUCCESS, String.format(Messages.DIRECTORY_CHANGED, folder));
				}
			}
		} catch (ConfigurationFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, String.format(Messages.DIRECTORY_CHANGE_FAILED));
		} catch (StorageMigrationFailedException e) {
			_controller.getUI().display(DisplayType.ERROR, String.format(Messages.DIRECTORY_CHANGE_FAILED));
		} catch (StorageLocationInvalidException e) {
			_controller.getUI().display(DisplayType.ERROR, String.format(Messages.DIRECTORY_INVALID));
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
