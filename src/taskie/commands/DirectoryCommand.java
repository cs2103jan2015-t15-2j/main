//@author A0121555M
package taskie.commands;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Messages;

public class DirectoryCommand implements ICommand {
	private CommandType _commandType = CommandType.DIRECTORY;

	public DirectoryCommand() {
	}

	@Override
	public CommandType getCommandType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() {
		try {
			String folder = Taskie.Controller.getUI().loadSelectDirectoryDialog(Taskie.Controller.getStorage().getStorageLocation());
			Taskie.Controller.getStorage().setStorageLocation(folder);
			Taskie.Controller.getUI().display(String.format(Messages.DIRECTORY_CHANGED_STRING, folder));
		} catch ( NullPointerException e ) {
			// Directory Change Cancelled
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType);
		return sb.toString();
	}
}
