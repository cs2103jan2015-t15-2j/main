//@author A0121555M
package taskie.parser;

import taskie.commands.ICommand;
import taskie.exceptions.InvalidCommandException;

public interface Parser {
	public ICommand parse(String input) throws InvalidCommandException;
}
