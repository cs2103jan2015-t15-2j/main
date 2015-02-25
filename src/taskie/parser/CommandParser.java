//@author A0121555M
package taskie.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Taskie;
import taskie.commands.AddCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.models.CommandType;
import taskie.models.Task;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.ParseLocation;

public class CommandParser implements Parser {
	private static String MESSAGE_INVALID_COMMAND = "Invalid Command";
	
	private static String[] KEYWORDS_DATETIME_SEPARATOR = new String[] { "from", "on", "between", "by", "in", "at", "on" };
	
	private static String[] COMMAND_KEYWORD_ADD = new String[] {"add", "create", "new", "ins", "insert", "put"};
	private static String[] COMMAND_KEYWORD_UPDATE = new String[] {"update", "change", "modify", "edit", "alter"};
	private static String[] COMMAND_KEYWORD_DELETE = new String[] {"delete", "clear", "remove"};
	private static String[] COMMAND_KEYWORD_VIEW_AND_SEARCH = new String[] {"search", "find", "look", "display", "show", "open", "view", "list"};
	private static String[] COMMAND_KEYWORD_UNDO = new String[] {"undo", "revert"};
	private static String[] COMMAND_KEYWORD_EXIT = new String[] {"exit", "quit", "close"};
	
	private com.joestelmach.natty.Parser _natty;
	private Logger _logger;
	
	public CommandParser() {
		_natty = new com.joestelmach.natty.Parser();
		_logger = Logger.getLogger(CommandParser.class.getName());
	}

	public void parse(String input) {
		String keyword = CommandParser.getCommandKeyword(input);
		String command = CommandParser.getCommandParameters(input);
		
		try {
			CommandType cmd = this.getCommandType(keyword);
			this.executeCommandType(cmd, command);
		} catch ( InvalidCommandException e ) {
			Taskie.UI.display(MESSAGE_INVALID_COMMAND);
		}
	}
	
	private CommandType getCommandType(String key) throws InvalidCommandException {
		CommandType commandType = null;
		
		if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_ADD) ) {
			commandType = CommandType.ADD;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_UPDATE) ) {
			commandType = CommandType.UPDATE;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_DELETE) ) {
			commandType = CommandType.DELETE;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_VIEW_AND_SEARCH) ) {
			commandType = CommandType.VIEW;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_UNDO) ) {
			commandType = CommandType.UNDO;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_EXIT) ) {
			commandType = CommandType.EXIT;
		}
		
		if ( commandType == null ) {
			throw new InvalidCommandException();
		}
		
		return commandType;
	}
	
	private void executeCommandType(CommandType cmd, String command) throws InvalidCommandException {
		command = command.trim();
		
		if ( cmd == CommandType.ADD ) {
			this.doAdd(command);
		} else if ( cmd == CommandType.UPDATE ) {
			this.doUpdate(command);
		} else if ( cmd == CommandType.DELETE ) {
			this.doDelete(command);
		} else if ( cmd == CommandType.VIEW ) {
			this.doView(command);
		} else if ( cmd == CommandType.UNDO ) {
			this.doUndo(command);
		} else if ( cmd == CommandType.EXIT ) {
			this.doExit();
		} else {
			throw new InvalidCommandException();
		}
	}
	
	private void doAdd(String command) {
		if ( command.isEmpty() ) {
			Taskie.UI.display(MESSAGE_INVALID_COMMAND);
			return;
		}
		
		List<DateGroup> groups = _natty.parse(command);
		Task task;
		
		if ( groups.size() > 0 ) {
			for ( DateGroup group : groups ) {
				String name1 = command.substring(0, group.getPosition()).trim();
				String name2 = command.substring(group.getPosition() + group.getText().length()).trim();
				List<Date> dates = group.getDates();
				
				String[] words = splitStringWithWhitespace(name1);
				int lastWord = words.length - 1;
				if( hasKeyword(words[lastWord], KEYWORDS_DATETIME_SEPARATOR) ) {
					name1 = command.substring(0, command.lastIndexOf(words[lastWord])).trim();
				}
				
				String name = name1 + " " + name2;
				_logger.log(Level.INFO, "Adding Task: " + name + "\n" + "Date Info Detected: " + group.getText() + "\n" + "Date Info Parsed: " + dates + "\n" + "Is Date Time Inferred: " + group.isTimeInferred());
				
				task = new Task(name);
				
				if ( dates.size() > 1 ) {
					// Timed tasks
					Date datetime1 = dates.get(0);
					Date datetime2 = dates.get(1);

					task = new Task(name);
					task.setStartDateTime(convertDateToLocalDateTime(datetime1));
					task.setEndDateTime(convertDateToLocalDateTime(datetime2));

					if ( group.isTimeInferred() ) {
						task.setStartTime(null);
						task.setEndTime(null);
					}

					Taskie.Controller.executeCommand(new AddCommand(task));
					Taskie.UI.display("Added " + name + " -- " + task.getStartDateTime() + " to " + task.getEndDateTime());
				} else {
					// Deadline tasks
					Date datetime1 = dates.get(0);
					task = new Task(name);
					task.setEndDateTime(convertDateToLocalDateTime(datetime1));

					if ( group.isTimeInferred() ) {
						task.setStartTime(null);
						task.setEndTime(null);
					}

					Taskie.Controller.executeCommand(new AddCommand(task));
					Taskie.UI.display("Added " + name + " -- " + task.getEndDateTime());
				}
			}
		} else {
			// Tasks without any deadlines
			String name = command.trim();
			
			task = new Task(name);
			Taskie.Controller.executeCommand(new AddCommand(task));
			Taskie.UI.display("Added " + name);
		}
	}
	
	private void doUpdate(String command) {
		if ( command.trim().isEmpty() ) {
			Taskie.UI.display("Invalid Command");
			return;
		}
		
		_natty.parse(command);
		Taskie.UI.display(command);
	}
	
	private void doDelete(String command) {
		
	}
	
	private void doView(String command) {
		if ( command.trim().isEmpty() ) {
			Taskie.UI.display("Invalid Command");
			return;
		}
		
		
	}

	private void doUndo(String command) {
		
	}

	private void doExit() {
		
	}
	
	private static boolean hasKeyword(String needle, String[] haystack) {
		needle = needle.toLowerCase();
		return (Arrays.asList(haystack).contains(needle));
	}
	
	private static String getCommandKeyword(String command) {
		return splitStringWithWhitespace(command)[0];
	}

	private static String getCommandParameters(String command) {
		return command.replace(getCommandKeyword(command) + " ", "").trim();
	}

	private static String[] splitStringWithWhitespace(String command) {
		return command.trim().split("\\s+");
	}

	private static LocalDateTime convertDateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
}
