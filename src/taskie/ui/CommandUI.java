//@author A0121555M
package taskie.ui;

import java.util.Scanner;

import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

public class CommandUI implements UI {
	private static int TASKS_PER_PAGE = 20;
	private static String MESSAGE_WELCOME = "Welcome to Taskie!";
	private static String MESSAGE_REQUEST_INPUT = "Enter Command: ";

	private Scanner _scanner;

	private Task[] _currentTaskList;


	public CommandUI() {
		_scanner = new Scanner(System.in);
		
	}


	public String readInput() {
		System.out.print(MESSAGE_REQUEST_INPUT);
		String input=null;
		if (_scanner.hasNextLine()) {
			input = _scanner.nextLine();

			if (input == null) {
				return null;
			}
		}
		return input;
	}

	public void display(Task task) {
		this.display(new Task[] { task });
	}

	public void display(Task[] tasks) {
		_currentTaskList = tasks;
		
		for ( int x = 0; x < tasks.length; x++ ) {
			Task task = tasks[x];
			
			System.out.print("#" + x + ": " );
			if ( task.isDeadlined() ) {
				System.out.println(task.getTitle() + " -- Complete by " + task.getEndDateTime());
			} else if ( task.isTimed() ) {
				System.out.println(task.getTitle() + " -- Do between " + task.getStartDateTime() + " to " + task.getEndDateTime());
			} else {
				System.out.println(task.getTitle());
			}
		}

	}

	public void display(String message) {
		System.out.println(message);
	}

	public Task[] getCurrentTaskList() {
		return _currentTaskList;
	}
	
	public void printWelcomeMessage() {
		this.display(MESSAGE_WELCOME);
	}



}
