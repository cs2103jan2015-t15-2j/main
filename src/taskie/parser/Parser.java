//@author A0121555M
package taskie.parser;

import taskie.exceptions.InvalidCommandException;

public interface Parser {
	public void parse(String input) throws InvalidCommandException;
}
