/**
 * class representing a undo command. 
 * Still under development
 * Bugs: none known
 *
 * @author       A0097582N
 */

package taskie.commands;

import taskie.models.CommandType;

public class UndoCommand implements ICommand {
	private CommandType _commandType = CommandType.UNDO;
<<<<<<< HEAD
	public UndoCommand(){
		
=======

	public UndoCommand() {

>>>>>>> upstream/master
	}

	@Override
	public CommandType getCommandType() {
		return _commandType;
	}
}
