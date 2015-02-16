//@author A0121555M
package taskie.ui;

import java.util.Scanner;

import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.NattyParser;
import taskie.parser.Parser;

public class CommandUI implements UI {
	private Scanner _scanner;
	private Parser _parser;

	public CommandUI() {
		_scanner = new Scanner(System.in);
		_parser = new CommandParser();

		this.readInput();
	}
	
	public void readInput() {
		String input = _scanner.nextLine();
		_parser.parse(input);
	}

	public void display(Task task) {
		
	}

	public void display(Task[] task) {
		
	}

	public void display(String message) {
		
	}
}
