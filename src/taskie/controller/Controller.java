/**
 * Class to call methods within each command to achieve functionality. Interface between front and back end.
 * Contains methods commonly used by individual command classes.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.controller;

import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.commands.ExitCommand;
import taskie.commands.ICommand;
import taskie.database.IStorage;
import taskie.database.Storage;
import taskie.exceptions.InvalidCommandException;
import taskie.models.CommandType;
import taskie.parser.CommandParser;
import taskie.parser.Parser;
import taskie.ui.CommandUI;
import taskie.ui.UI;

public class Controller {
	private Logger _logger;
	private Parser _parser;
	private Stack<ICommand> _undoStack;
	private Stack<ICommand> _redoStack;

	protected UI _ui;
	protected IStorage _storage;

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
		_logger = Logger.getLogger(Controller.class.getName());
		_undoStack = new Stack<ICommand>();
		_redoStack = new Stack<ICommand>();
		_ui = new CommandUI();
		_parser = new CommandParser();
		_storage = new Storage();
	}

	public void run() {
		_ui.run();

		while (_ui.isUIRunning()) {
			String string = _ui.readInput();
			if (string == null) {
				this.executeCommand(new ExitCommand());
			} else {
				if (!string.isEmpty()) {
					try {
						ICommand cmd = _parser.parse(string);
						this.executeCommand(cmd);
					} catch (InvalidCommandException e) {
						_ui.display(taskie.models.Messages.INVALID_COMMAND);
					}
				}
			}
		}
	}

	public void executeCommand(ICommand command) {
		addTaskHistory(command);
		command.execute();
	}

	//@author A0097582N-unused
	//Reason for unused: Moved to Task Model by Yunheng (code commented because it will break existing code)
	/*
	public String determineTaskType(Task task) {
		if (task.getStartDate() == null && task.getEndDate() == null) {
			return FLOATING_TASKNAME;
		} else if (task.getStartDate() == null && task.getEndDate() != null) {
			return DEADLINED_TASKNAME;
		} else {
			return TIMED_TASKNAME;
		}
	}
	*/
	
	//@author A0121555M
	public Stack<ICommand> getUndoStack() {
		return _undoStack;
	}

	public Stack<ICommand> getRedoStack() {
		return _redoStack;
	}

	private void addTaskHistory(ICommand command) {
		CommandType type = command.getCommandType();
		if ( type == CommandType.ADD ||  type == CommandType.UPDATE || type == CommandType.DELETE || type == CommandType.MARK || type == CommandType.UNMARK  ) {
			_logger.log(Level.INFO, "Adding to Undo: " + command);
			_undoStack.add(command);
		}
	}
}