/**
 * interface to be implemented by all command classes. 
 * Still under development
 * Bugs: none known
 *
 */
// @author       A0097582N
 

package taskie.commands;

import taskie.models.CommandType;

public interface ICommand {
	public CommandType getCommandType();
}
