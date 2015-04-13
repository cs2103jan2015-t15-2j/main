/**
 * Class to call methods within each command to achieve functionality. Interface between front and back end.
 * Contains methods commonly used by individual command classes.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie;

import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.commands.ExitCommand;
import taskie.commands.ICommand;
import taskie.database.Configuration;
import taskie.database.IStorage;
import taskie.database.NStorage;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;
import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.Parser;
import taskie.ui.CommandUI;
import taskie.ui.UI;

public class Controller {
	private static Controller _instance;
	private Configuration _config;
	private Logger _logger;
	private Parser _parser;
	private Stack<ICommand> _undoStack;
	private Stack<ICommand> _redoStack;
	private Task _lastTask;

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

	public Configuration getConfiguration() {
		return _config;
	}

	private Controller() {
		_logger = Logger.getLogger(Controller.class.getName());
		_config = Configuration.getInstance();
		_ui = new CommandUI(this);
		_parser = new CommandParser();
		this.initialize();
	}

	private void initialize() {
		try {
			_undoStack = new Stack<ICommand>();
			_redoStack = new Stack<ICommand>();
			_storage = new NStorage(_config.getDatabasePath());
			_lastTask = null;
		} catch (IOException e) {
			_logger.log(Level.SEVERE, Messages.STORAGE_INITIALISATION_ERROR);
			System.out.println(Messages.STORAGE_INITIALISATION_ERROR);
		}
	}
	
	public static Controller getInstance() {
		if (_instance == null) {
			_instance = new Controller();
		}
		return _instance;
	}

	public Task getLastTask() throws TaskRetrievalFailedException {
		if (_lastTask == null) {
			throw new TaskRetrievalFailedException();
		}
		return _lastTask;
	}

	public void setLastTask(Task task) {
		_lastTask = task;
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
						_ui.display(DisplayType.ERROR, Messages.INVALID_COMMAND);
					}
				}
			}
		}
	}

	public void executeCommand(ICommand command) {
		boolean success = command.execute();
		if ( success ) { 
			addTaskHistory(command);
		}
	}

	// @author A0121555M
	public Stack<ICommand> getUndoStack() {
		return _undoStack;
	}

	public Stack<ICommand> getRedoStack() {
		return _redoStack;
	}

	private void addTaskHistory(ICommand command) {
		CommandType type = command.getCommandType();
		if (type == CommandType.ADD || type == CommandType.UPDATE || type == CommandType.DELETE || type == CommandType.MARK || type == CommandType.UNMARK) {
			_logger.log(Level.INFO, "Adding to Undo: " + command);
			_undoStack.add(command);
		}
	}

	// @author A0135137L
	public void resetUndoAndRedoStacks() {
		_undoStack = new Stack<ICommand>();
		_redoStack = new Stack<ICommand>();
	}
}