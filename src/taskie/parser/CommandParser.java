//@author A0121555M
package taskie.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Taskie;
import taskie.commands.AddCommand;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.ViewType;

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
	
	private static String[] VIEW_KEYWORDS_ALL = new String[] {"", "all", "everything"};
	private static String[] VIEW_KEYWORDS_UPCOMING = new String[] {"upcoming", "incomplete", "undone", "todo"};
	private static String[] VIEW_KEYWORDS_COMPLETED = new String[] {"completed", "complete", "done"};
	private static String[] VIEW_KEYWORDS_OVERDUE = new String[] {"overdue", "due", "urgent", "late"};
	
	private static String[] SEARCH_RELATIVITY_BEFORE = new String[] { "before" };
	private static String[] SEARCH_RELATIVITY_AFTER = new String[] { "after" };
	private static String[] SEARCH_RELATIVITY_EXACT = new String[] { "on" };
	private static String[] SEARCH_RELATIVITY_SPECIFIED = new String[] { "between", "from" };
	private static String[] SEARCH_RELATIVITY_RELATIVE = new String [] { "this", "rest of", "in" };
	
	private static LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static LocalDateTime MAX_DATETIME = LocalDateTime.MAX;
	
	private enum RelativeType { BEFORE, AFTER, EXACT, SPECIFIED, RELATIVE, NONE };
	
	private com.joestelmach.natty.Parser _natty;
	private Logger _logger;
	private HashMap<String, ViewType> dictViewTypes;
	private HashMap<String, RelativeType> dictRelativeTypes;
	
	public CommandParser() {
		_natty = new com.joestelmach.natty.Parser();
		_logger = Logger.getLogger(CommandParser.class.getName());
		
		initializeDictionaries();
	}
	
	private void initializeDictionaries() { 
		dictViewTypes = new HashMap<String, ViewType>();
		dictRelativeTypes = new HashMap<String, RelativeType>();
		
		for ( String word : VIEW_KEYWORDS_ALL ) {
			dictViewTypes.put(word, ViewType.ALL);
		}
		
		for ( String word : VIEW_KEYWORDS_UPCOMING ) {
			dictViewTypes.put(word, ViewType.UPCOMING);
		}

		for ( String word : VIEW_KEYWORDS_COMPLETED ) {
			dictViewTypes.put(word, ViewType.COMPLETED);
		}

		for ( String word : VIEW_KEYWORDS_OVERDUE ) {
			dictViewTypes.put(word, ViewType.OVERDUE);
		}
		
		for ( String word : SEARCH_RELATIVITY_BEFORE ) {
			dictRelativeTypes.put(word, RelativeType.BEFORE);
		}
		
		for ( String word : SEARCH_RELATIVITY_AFTER ) {
			dictRelativeTypes.put(word, RelativeType.AFTER);
		}

		for ( String word : SEARCH_RELATIVITY_EXACT ) {
			dictRelativeTypes.put(word, RelativeType.EXACT);
		}

		for ( String word : SEARCH_RELATIVITY_SPECIFIED ) {
			dictRelativeTypes.put(word, RelativeType.SPECIFIED);
		}

		for ( String word : SEARCH_RELATIVITY_RELATIVE ) {
			dictRelativeTypes.put(word, RelativeType.RELATIVE);
		}
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
	
	private RelativeType getRelativeType(String key) {
		RelativeType relativeType = RelativeType.NONE;
		
		if ( hasKeyword(key, CommandParser.SEARCH_RELATIVITY_BEFORE) ) {
			relativeType = RelativeType.BEFORE;
		} else if ( hasKeyword(key, CommandParser.SEARCH_RELATIVITY_AFTER) ) {
			relativeType = RelativeType.AFTER;
		} else if ( hasKeyword(key, CommandParser.SEARCH_RELATIVITY_EXACT) ) {
			relativeType = RelativeType.EXACT;
		} else if ( hasKeyword(key, CommandParser.SEARCH_RELATIVITY_SPECIFIED) ) {
			relativeType = RelativeType.SPECIFIED;
		} else if ( hasKeyword(key, CommandParser.SEARCH_RELATIVITY_RELATIVE) ) {
			relativeType = RelativeType.RELATIVE;
		}
		
		return relativeType;
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
				
				String name = (name1 + " " + name2).trim();
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
		if ( dictViewTypes.containsKey(command) ) {
			Taskie.UI.display(MESSAGE_INVALID_COMMAND);
			return;
		}
		
		String type = CommandParser.getCommandKeyword(command);
		String query = CommandParser.getCommandParameters(command);
		
		ViewType viewType = dictViewTypes.get(type);
		if ( viewType == ViewType.SEARCH ) {
			List<DateGroup> groups = _natty.parse(query);
			if ( groups.size() > 0 ) {
				// Search by Date and possibly keywords
				for ( DateGroup group : groups ) {
					String query1 = command.substring(0, group.getPosition()).trim();
					String query2 = command.substring(group.getPosition() + group.getText().length()).trim();
					List<Date> dates = group.getDates();
					
					String[] words = splitStringWithWhitespace(query1);
					int lastWord = words.length - 1;
					if( hasKeyword(words[lastWord], KEYWORDS_DATETIME_SEPARATOR) ) {
						query1 = command.substring(0, command.lastIndexOf(words[lastWord])).trim();
					}
					
					if ( dates.size() > 1 ) {
						Date datetime1 = dates.get(0);
						Date datetime2 = dates.get(1);
						LocalDateTime startDateTime = convertDateToLocalDateTime(datetime1);
						LocalDateTime endDateTime = convertDateToLocalDateTime(datetime2);
						
						ViewCommand viewCommand = new ViewCommand(viewType);
						viewCommand.setStartDateTime(startDateTime);
						viewCommand.setEndDateTime(endDateTime);
						
						if ( group.isTimeInferred() ) {
							viewCommand.setStartTime(null);
							viewCommand.setEndTime(null);
						}
						
						Taskie.Controller.executeCommand(viewCommand);
					} else {
						Date datetime1 = dates.get(0);
						LocalDateTime searchDateTime = convertDateToLocalDateTime(datetime1);

						ViewCommand viewCommand = new ViewCommand(viewType);
						viewCommand.setStartDateTime(searchDateTime);

						if ( group.isTimeInferred() ) {
							viewCommand.setStartTime(null);
							viewCommand.setEndTime(null);
						}
						
						Taskie.Controller.executeCommand(viewCommand);
						Taskie.UI.display("Viewing " + viewType);
					}
				}
			} else {
				// Keyword only search
				ViewCommand viewCommand = new ViewCommand(viewType);

				Taskie.Controller.executeCommand(viewCommand);
				Taskie.UI.display("Viewing " + viewType);
			}
		} else {
			Taskie.Controller.executeCommand(new ViewCommand(viewType));
			Taskie.UI.display("Viewing " + viewType);
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
