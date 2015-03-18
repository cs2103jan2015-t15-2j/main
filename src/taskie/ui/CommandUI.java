//@author A0121555M
package taskie.ui;

import java.util.Scanner;

import taskie.exceptions.InvalidTaskException;
import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

public class CommandUI implements UI {
	private static int TASKS_PER_PAGE = 20;
	private static String MESSAGE_WELCOME = "Welcome to Taskie!";
	private static String MESSAGE_REQUEST_INPUT = "Enter Command: ";

	private Scanner _scanner;
	private Task[] _currentTaskList;
	private static boolean _isInitialized;
	private static boolean _isUIRunning;

	public CommandUI() {
		_scanner = new Scanner(System.in);
		_isInitialized = false;
	}

	public String readInput() {
		System.out.print(MESSAGE_REQUEST_INPUT);
		String input = null;
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

		for (int x = 0; x < tasks.length; x++) {
			Task task = tasks[x];

			System.out.print("#" + (x + 1) + ": ");
			if (task.isDeadlined()) {
				System.out.println(task.getTitle() + " -- Complete by "
						+ task.getEndDateTime());
			} else if (task.isTimed()) {
				System.out.println(task.getTitle() + " -- Do between "
						+ task.getStartDateTime() + " to "
						+ task.getEndDateTime());
			} else {
				System.out.println(task.getTitle());
			}
		}

	}

	public void display(String message) {
		System.out.println(message);
	}

	public Task getTask(int index) throws InvalidTaskException {
		index = index - 1;

		if (_currentTaskList == null) {
			throw new InvalidTaskException();
		}

		assert _currentTaskList != null;

		if (_currentTaskList.length > index && _currentTaskList[index] != null) {
			assert _currentTaskList[index] != null;
			return _currentTaskList[index];
		}

		throw new InvalidTaskException();
	}

	public Task[] getCurrentTaskList() throws InvalidTaskException {
		if (_currentTaskList == null) {
			throw new InvalidTaskException();
		}

		assert _currentTaskList != null;
		return _currentTaskList;
	}

	private void printWelcomeMessage() {
		this.display(MESSAGE_WELCOME);
	}

	public void run() {
		if (!_isInitialized) {
			_isInitialized = true;
			this.printWelcomeMessage();
			_isUIRunning = true;
		}
	}

	public void exit() {
		_isUIRunning = false;
	}

	public boolean isUIRunning() {
		return _isUIRunning;
	}

}
