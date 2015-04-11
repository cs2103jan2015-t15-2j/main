//@author A0121555M
package taskie.parser;

import taskie.commands.ICommand;
import taskie.exceptions.InvalidCommandException;

/**
 * Parser is an interface for implementing different parsers for reading user input
 */
public interface Parser {
	public ICommand parse(String input) throws InvalidCommandException;
}
