/**
 * interface to be implemented by all command classes. 
 * Still under development
 * Bugs: none known
 */
//@author A0097582N

package taskie.commands;

import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;

public interface ICommand {
	public CommandType getCommandType();

	public boolean execute();
	
	public void undo() throws UndoNotSupportedException;
}
