/**
 * Class to call methods within each command to achieve functionality. Interface between front and back end.
 * Contains methods commonly used by individual command classes.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import taskie.commands.ICommand;
import taskie.database.IStorage;
import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.Parser;
import taskie.ui.CommandUI;
import taskie.ui.UI;
import taskie.database.Storage;
import taskie.exceptions.InvalidCommandException;

public class Controller {
	private UI _ui;
	private IStorage _storage;
	private Parser _parser;

	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";

	public UI getUI() {
		return _ui;
	}

	public IStorage getStorage() {
		return _storage;
	}

	public Parser getParser() {
		return _parser;
	}

	public Controller() {
	}

	public void run() {
		_ui = new CommandUI();
		_parser = new CommandParser();
		_storage = new Storage();

		_ui.run();
		
		while ( _ui.isUIRunning() ) {
			String string = _ui.readInput();
			try {
				_parser.parse(string);
			} catch (InvalidCommandException e) {
				_ui.display(taskie.models.Messages.INVALID_COMMAND);
			}
		}
	}

	public void executeCommand(ICommand command) {
		command.execute();

	}

	public String formatTime(LocalDate startDate, LocalTime startTime) {
		String string = "";
		if (startDate != null) {
			string = string.concat(startDate.toString());
		}

		if (startTime != null) {
			string = string.concat(" " + startTime.getHour() + " "
					+ startTime.getMinute());
		}
		return string;
	}

	public String determineTaskType(Task task) {
		if (task.getStartDate() == null && task.getEndDate() == null) {
			return FLOATING_TASKNAME;
		} else if (task.getStartDate() == null && task.getEndDate() != null) {
			return DEADLINED_TASKNAME;
		} else {
			return TIMED_TASKNAME;
		}
	}

}