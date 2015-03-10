//@author A0121555M
package taskie.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Taskie;
import taskie.commands.AddCommand;
import taskie.commands.DeleteCommand;
import taskie.commands.ExitCommand;
import taskie.commands.MarkCommand;
import taskie.commands.RedoCommand;
import taskie.commands.UndoCommand;
import taskie.commands.UnmarkCommand;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.ViewType;

import com.joestelmach.natty.DateGroup;

public class CommandParser implements Parser {
	private static final String MESSAGE_INVALID_COMMAND = "Invalid Command";
	
	private static final String[] KEYWORDS_DATETIME_SEPARATOR = new String[] { "from", "on", "between", "by", "in", "at", "on" };
	
	private static final String[] COMMAND_KEYWORD_ADD = new String[] {"add", "create", "new", "ins", "insert", "put"};
	private static final String[] COMMAND_KEYWORD_UPDATE = new String[] {"update", "change", "modify", "edit", "alter"};
	private static final String[] COMMAND_KEYWORD_DELETE = new String[] {"delete", "clear", "remove"};
	private static final String[] COMMAND_KEYWORD_VIEW_AND_SEARCH = new String[] {"search", "find", "look", "display", "show", "open", "view", "list"};
	private static final String[] COMMAND_KEYWORD_UNDO = new String[] {"undo", "revert"};
	private static final String[] COMMAND_KEYWORD_REDO = new String[] {"redo"};
	private static final String[] COMMAND_KEYWORD_MARK = new String[] {"mark", "complete", "done", "check"};
	private static final String[] COMMAND_KEYWORD_UNMARK = new String[] {"unmark", "incomplete", "undone", "uncheck"};
	private static final String[] COMMAND_KEYWORD_EXIT = new String[] {"exit", "quit", "close"};
	
	private static final String[] VIEW_KEYWORDS_ALL = new String[] {"", "all", "everything"};
	private static final String[] VIEW_KEYWORDS_UPCOMING = new String[] {"upcoming", "incomplete", "undone", "todo"};
	private static final String[] VIEW_KEYWORDS_COMPLETED = new String[] {"completed", "complete", "done"};
	private static final String[] VIEW_KEYWORDS_OVERDUE = new String[] {"overdue", "due", "urgent", "late"};
	
	private static final String[] SEARCH_RELATIVITY_BEFORE = new String[] { "before" };
	private static final String[] SEARCH_RELATIVITY_AFTER = new String[] { "after" };
	private static final String[] SEARCH_RELATIVITY_EXACT = new String[] { "on" };
	private static final String[] SEARCH_RELATIVITY_SPECIFIED = new String[] { "between", "from" };
	
	private static final LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static final LocalDateTime MAX_DATETIME = LocalDateTime.MAX;
	
	private static final int NUM_START_END_DATETIME = 2;
	private static final int START_DATETIME = 0;
	private static final int END_DATETIME = 1;
	
	private enum RelativeType { BEFORE, AFTER, EXACT, SPECIFIED, NONE };
	
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
	}

	public void parse(String input) {
		String keyword = CommandParser.getCommandKeyword(input);
		String command = CommandParser.getCommandParameters(input);
		
		try {
			CommandType cmd = this.getCommandType(keyword);
			this.executeCommandType(cmd, command);
		} catch ( InvalidCommandException e ) {
			Taskie.Controller.getUI().display(MESSAGE_INVALID_COMMAND);
		}
	}
	
	private ViewType getViewType(String key) {
		ViewType viewType = dictViewTypes.get(key);
		return viewType == null ? ViewType.SEARCH : viewType;
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
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_REDO) ) {
			commandType = CommandType.REDO;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_MARK) ) {
			commandType = CommandType.MARK;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_UNMARK) ) {
			commandType = CommandType.UNMARK;
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
		} else if ( cmd == CommandType.REDO ) {
			this.doRedo(command);
		} else if ( cmd == CommandType.MARK ) {
			this.doMark(command);
		} else if ( cmd == CommandType.UNMARK ) {
			this.doUnmark(command);
		} else if ( cmd == CommandType.EXIT ) {
			this.doExit();
		} else {
			throw new InvalidCommandException();
		}
	}
	
	private void doAdd(String command) {
		if ( command.isEmpty() ) {
			Taskie.Controller.getUI().display(MESSAGE_INVALID_COMMAND);
			return;
		}
		
		List<DateGroup> groups = _natty.parse(command);
		
		if ( groups.size() > 0 ) {
			DateGroup group = groups.get(0);
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

			LocalDateTime[] startAndEndDateTime = getStartAndEndDateTime(dates);

			AddCommand cmd = new AddCommand();
			cmd.setTaskName(name);
			
			if ( startAndEndDateTime[START_DATETIME] != null ) {
				cmd.setStartDate(startAndEndDateTime[START_DATETIME].toLocalDate());
				
				if ( group.isTimeInferred() ) {
					cmd.setStartTime(null);
				} else {
					cmd.setStartTime(startAndEndDateTime[START_DATETIME].toLocalTime());
				}
			}
			
			if ( startAndEndDateTime[END_DATETIME] != null ) {
				cmd.setEndDate(startAndEndDateTime[END_DATETIME].toLocalDate());
				if ( group.isTimeInferred() ) {
					cmd.setEndTime(null);
				} else {
					cmd.setEndTime(startAndEndDateTime[END_DATETIME].toLocalTime());
				}
			}
			_logger.log(Level.INFO, "Added {0} -- {1} to {2}", new Object[] { cmd.getTaskName(), (cmd.getStartDateTime() == null ? "null" : cmd.getStartDateTime()), (cmd.getEndDateTime() == null ? "null" : cmd.getEndDateTime()) });
			Taskie.Controller.executeCommand(cmd);
		} else {
			// Tasks without any deadlines
			String name = command.trim();
			
			AddCommand cmd = new AddCommand();
			cmd.setTaskName(name);
			_logger.log(Level.INFO, "Added {0} - No date and time set", cmd.getTaskName());
			Taskie.Controller.executeCommand(cmd);
		}
	}
	
	private void doUpdate(String command) {
		if ( command.trim().isEmpty() ) {
			Taskie.Controller.getUI().display(MESSAGE_INVALID_COMMAND);
			return;
		}
		
		_natty.parse(command);
		Taskie.Controller.getUI().display(command);
	}
	
	private void doDelete(String command) {
		int itemNumber = Integer.parseInt(command);
		_logger.log(Level.INFO, "Deleting Task: {0}", itemNumber);
		Taskie.Controller.executeCommand(new DeleteCommand(itemNumber));
	}
	
	private void doView(String command) {
		String keywords;
		
		String keyword = CommandParser.getCommandKeyword(command);
		String query = CommandParser.getCommandParameters(command);

		ViewType viewType = getViewType(keyword);
		
		if ( viewType == ViewType.SEARCH ) {
			keywords = keyword + " " + query;
		} else {
			keywords = query;		
		}
		
		List<DateGroup> groups = _natty.parse(keywords);

		if ( groups.size() > 0 ) {
			// Search by Date and possibly keywords
			DateGroup group = groups.get(0);
			List<Date> dates = group.getDates();
			LocalDateTime[] startAndEndDateTime = getStartAndEndDateTime(dates);
			RelativeType relativeType = RelativeType.NONE;

			if ( viewType == ViewType.SEARCH ) {
				String keyword1 = keywords.substring(0, group.getPosition()).trim();
				String keyword2 = keywords.substring(group.getPosition() + group.getText().length()).trim();

				String[] words = splitStringWithWhitespace(keyword1);
				int lastWord = words.length - 1;
				relativeType = getRelativeType(words[lastWord]);
				if( relativeType != RelativeType.NONE ) {
					keyword1 = keyword1.substring(0, keyword1.lastIndexOf(words[lastWord])).trim();
				}
				
				keywords = (keyword1 + " " + keyword2).trim();				
			} else {
				String[] words = splitStringWithWhitespace(keywords);
				int lastWord = words.length - 1;
				relativeType = getRelativeType(words[lastWord]);
				if( relativeType != RelativeType.NONE ) {
					keywords = keywords.substring(0, keywords.lastIndexOf(words[lastWord])).trim();
				}
				
				keywords = keywords.trim();
			}

			_logger.log(Level.INFO, "View Type: " + viewType + "\nRelative Type: " + relativeType + "\nKeywords: " + keywords + "\n" + "Date Info Detected: " + group.getText() + "\n" + "Date Info Parsed: " + dates + "\n" + "Is Date Time Inferred: " + group.isTimeInferred());

			ViewCommand viewCommand = new ViewCommand(viewType);
			viewCommand.setSearchKeywords(keywords);
			
			if ( relativeType == RelativeType.BEFORE ) {
				viewCommand.setStartDateTime(MIN_DATETIME);
				viewCommand.setEndDateTime(startAndEndDateTime[START_DATETIME]);
			} else if ( relativeType == RelativeType.AFTER ) {
				viewCommand.setStartDateTime(startAndEndDateTime[START_DATETIME]);
				viewCommand.setEndDateTime(MAX_DATETIME);
			} else if ( relativeType == RelativeType.EXACT ) {
				viewCommand.setStartDateTime(startAndEndDateTime[START_DATETIME]);
				viewCommand.setEndDateTime(startAndEndDateTime[END_DATETIME]);
				viewCommand.setStartTime(null);
				viewCommand.setEndTime(null);
			} else if ( relativeType == RelativeType.SPECIFIED ) {
				viewCommand.setStartDateTime(startAndEndDateTime[START_DATETIME]);
				viewCommand.setEndDateTime(startAndEndDateTime[END_DATETIME]);
			}
			
			if ( group.isTimeInferred() ) {
				viewCommand.setStartTime(null);
				viewCommand.setEndTime(null);
			}
			
			Taskie.Controller.executeCommand(viewCommand);
		} else {
			ViewCommand viewCommand = new ViewCommand(viewType);
			viewCommand.setSearchKeywords(keywords);
			_logger.log(Level.INFO, "Searching for tasks with keywords: {0}", keywords);
			Taskie.Controller.executeCommand(viewCommand);
		}
	}

	private void doUndo(String command) {
		int steps = 1;
		try {
			steps = Integer.parseInt(command);
			steps = (steps > 0) ? steps : 1;
		} catch ( NumberFormatException ex ) {
			steps = 1;
		}
		
		_logger.log(Level.INFO, "Undo (Steps: {0})", steps);
		Taskie.Controller.executeCommand(new UndoCommand(steps));
	}
	
	private void doRedo(String command) {
		int steps = 1;
		try {
			steps = Integer.parseInt(command);
			steps = (steps > 0) ? steps : 1;
		} catch ( NumberFormatException ex ) {
			steps = 1;
		}
		
		_logger.log(Level.INFO, "Redo (Steps: {0})", steps);
		Taskie.Controller.executeCommand(new RedoCommand(steps));
	}
	
	private void doMark(String command) {
		int itemNumber = Integer.parseInt(command);

		Task[] tasks = Taskie.Controller.getUI().getCurrentTaskList();
		try {
			Task task = tasks[itemNumber];
			_logger.log(Level.INFO, "Marking Task: {0} as Complete", task.getTitle());
			Taskie.Controller.executeCommand(new MarkCommand(task));
		} catch ( ArrayIndexOutOfBoundsException ex ) {
			Taskie.Controller.getUI().display("Invalid Task Number");
		}
	}
	
	private void doUnmark(String command) {
		int itemNumber = Integer.parseInt(command);

		Task[] tasks = Taskie.Controller.getUI().getCurrentTaskList();
		try {
			Task task = tasks[itemNumber];
			_logger.log(Level.INFO, "Unmarking Task: {0} as Complete", task.getTitle());
			Taskie.Controller.executeCommand(new UnmarkCommand(task));
		} catch ( ArrayIndexOutOfBoundsException ex ) {
			Taskie.Controller.getUI().display("Invalid Task Number");
		}
	}

	private void doExit() {
		_logger.log(Level.INFO, "Exiting Taskie");
		Taskie.Controller.executeCommand(new ExitCommand());
	}
	
	private static LocalDateTime[] getStartAndEndDateTime(List<Date> dates) {
		LocalDateTime[] startAndEndDateTime = new LocalDateTime[NUM_START_END_DATETIME];

		if ( dates.size() > 1 ) {
			Date datetime1 = dates.get(0);
			Date datetime2 = dates.get(1);
			
			LocalDateTime ldt1 = convertDateToLocalDateTime(datetime1);
			LocalDateTime ldt2 = convertDateToLocalDateTime(datetime2);
			
			if ( ldt1.isAfter(ldt2) ) {
				startAndEndDateTime[START_DATETIME] = ldt2;
				startAndEndDateTime[END_DATETIME] = ldt1;
			} else {
				startAndEndDateTime[START_DATETIME] = ldt1;
				startAndEndDateTime[END_DATETIME] = ldt2;
			}
		} else if ( dates.size() == 1 ) {
			// Deadline tasks

			Date datetime1 = dates.get(0);
			LocalDateTime ldt1 = convertDateToLocalDateTime(datetime1);
			startAndEndDateTime[START_DATETIME] = null;
			startAndEndDateTime[END_DATETIME] = ldt1;
		}
		
		return startAndEndDateTime;	
	}
	
	private static boolean hasKeyword(String needle, String[] haystack) {
		needle = needle.toLowerCase();
		return (Arrays.asList(haystack).contains(needle));
	}
	
	private static String getCommandKeyword(String command) {
		return splitStringWithWhitespace(command)[0];
	}

	private static String getCommandParameters(String command) {
		return command.replace(getCommandKeyword(command), "").trim();
	}

	private static String[] splitStringWithWhitespace(String command) {
		return command.trim().split("\\s+");
	}

	private static LocalDateTime convertDateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
}
