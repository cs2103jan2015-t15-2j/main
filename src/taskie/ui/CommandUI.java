//@author A0121555M
package taskie.ui;

import java.util.Scanner;

import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.NattyParser;
import taskie.parser.Parser;

public class CommandUI implements UI {
	private static String MESSAGE_WELCOME = "Welcome to Taskie!";
	private static String MESSAGE_REQUEST_INPUT = "Enter Command: ";
	
	private Scanner _scanner;
	private Parser _parser;
	private static boolean _isInitialized;

	public CommandUI() {
		_scanner = new Scanner(System.in);
		_parser = new CommandParser();
		_isInitialized = false;
	}
	
	public void run() {
		if ( !_isInitialized ) {
			_isInitialized = true;
			this.printWelcomeMessage();
			while (true) {
				this.readInput();
			}
		}
	}
	
	public void readInput() {
		System.out.print(MESSAGE_REQUEST_INPUT);
		String input = _scanner.nextLine();
		_parser.parse(input);
	}

	public void display(Task task) {
		
	}

	public void display(Task[] task) {
		
	}

	public void display(String message) {
		System.out.println(message);
	}

	private void printWelcomeMessage() {
		System.out.println(MESSAGE_WELCOME);
	}
}
