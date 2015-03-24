//@author A0121555M
package taskie.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import taskie.commands.AddCommand;
import taskie.commands.DeleteCommand;
import taskie.commands.DirectoryCommand;
import taskie.commands.ExitCommand;
import taskie.commands.ICommand;
import taskie.commands.MarkCommand;
import taskie.commands.RedoCommand;
import taskie.commands.UndoCommand;
import taskie.commands.UnmarkCommand;
import taskie.commands.UpdateCommand;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.models.CommandType;
import taskie.models.ViewType;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.ParseLocation;

public class CommandParser implements Parser {
	private static final String[] KEYWORDS_DATETIME_SEPARATOR = new String[] { "from", "on", "between", "by", "in", "at", "on", "due" };
	
	private static final String[] COMMAND_KEYWORD_ADD = new String[] {"add", "create", "new", "ins", "insert", "put"};
	private static final String[] COMMAND_KEYWORD_UPDATE = new String[] {"update", "change", "modify", "edit", "alter"};
	private static final String[] COMMAND_KEYWORD_DELETE = new String[] {"delete", "clear", "remove", "rm", "discard", "del"};
	private static final String[] COMMAND_KEYWORD_VIEW_AND_SEARCH = new String[] {"search", "find", "look", "display", "show", "open", "view", "list", "ls"};
	private static final String[] COMMAND_KEYWORD_UNDO = new String[] {"undo", "revert"};
	private static final String[] COMMAND_KEYWORD_REDO = new String[] {"redo"};
	private static final String[] COMMAND_KEYWORD_MARK = new String[] {"mark", "complete", "done", "check"};
	private static final String[] COMMAND_KEYWORD_UNMARK = new String[] {"unmark", "incomplete", "undone", "uncheck"};
	private static final String[] COMMAND_KEYWORD_DIRECTORY = new String[] {"directory"};
	private static final String[] COMMAND_KEYWORD_EXIT = new String[] {"exit", "quit", "close"};
	
	private static final String[] VIEW_KEYWORDS_ALL = new String[] {"", "all", "everything"};
	private static final String[] VIEW_KEYWORDS_UPCOMING = new String[] {"upcoming", "incomplete", "undone", "todo"};
	private static final String[] VIEW_KEYWORDS_COMPLETED = new String[] {"completed", "complete", "done"};
	private static final String[] VIEW_KEYWORDS_OVERDUE = new String[] {"overdue", "due", "urgent", "late"};
	
	private static final String[] SEARCH_RELATIVITY_BEFORE = new String[] { "before", "b4" };
	private static final String[] SEARCH_RELATIVITY_AFTER = new String[] { "after", "aft" };
	private static final String[] SEARCH_RELATIVITY_EXACT = new String[] { "on" };
	private static final String[] SEARCH_RELATIVITY_SPECIFIED = new String[] { "between", "from" };
	
	private static final LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static final LocalDateTime MAX_DATETIME = LocalDateTime.MAX;
	
	private static final int NUM_START_END_DATETIME = 2;
	private static final int DATETIME_START = 0;
	private static final int DATETIME_END = 1;
	private enum RelativeType { BEFORE, AFTER, EXACT, SPECIFIED, NONE };
	
    private static final String PATTERN_DOT_SEPARATED_TIME = "\\d{1,2}[.]\\d{2}";
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

	public ICommand parse(String input) throws InvalidCommandException {
		String keyword = CommandParser.getCommandKeyword(input);
		String command = CommandParser.getCommandParameters(input);
		
		CommandType cmd = this.getCommandType(keyword);
		assert cmd != null;
		return this.executeCommandType(cmd, command);
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
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_DIRECTORY) ) {
			commandType = CommandType.DIRECTORY;
		} else if ( hasKeyword(key, CommandParser.COMMAND_KEYWORD_EXIT) ) {
			commandType = CommandType.EXIT;
		}
		
		if ( commandType == null ) {
			throw new InvalidCommandException();
		}
		
		return commandType;
	}
	
	private ICommand executeCommandType(CommandType cmd, String command) throws InvalidCommandException {
		command = command.trim();
		
		if ( cmd == CommandType.ADD ) {
			return this.doAdd(command);
		} else if ( cmd == CommandType.UPDATE ) {
			return this.doUpdate(command);
		} else if ( cmd == CommandType.DELETE ) {
			return this.doDelete(command);
		} else if ( cmd == CommandType.VIEW ) {
			return this.doView(command);
		} else if ( cmd == CommandType.UNDO ) {
			return this.doUndo(command);
		} else if ( cmd == CommandType.REDO ) {
			return this.doRedo(command);
		} else if ( cmd == CommandType.MARK ) {
			return this.doMark(command);
		} else if ( cmd == CommandType.UNMARK ) {
			return this.doUnmark(command);
		} else if ( cmd == CommandType.DIRECTORY ) {
			return this.doDirectory();
		} else if ( cmd == CommandType.EXIT ) {
			return this.doExit();
		} else {
			throw new InvalidCommandException();
		}
	}
	
	private String determineTaskName(String command, DateGroup group) {
		_logger.log(Level.INFO, "Determining Task Name from: " + command + "\nDateGroup Start at position: " + group.getPosition());

		String name1 = command.substring(0, group.getPosition());
		String name2 = command.substring(group.getPosition() + group.getText().length());
		
		// Workaround a parsing Natty bug
		Map<String, List<ParseLocation>> parseMap = group.getParseLocations();
		List<ParseLocation> explicit_time = parseMap.get("explicit_time");
	private String reformatDateAndTime(String date) {
		Pattern timePattern = Pattern.compile(PATTERN_DOT_SEPARATED_TIME);
		Matcher timePatternMatcher = timePattern.matcher(date);
		while ( timePatternMatcher.find() ) {
			String matchingGroup = timePatternMatcher.group(0);
            String newTime = matchingGroup.replace(".", ":");
            date = date.replace(timePatternMatcher.group(0), newTime);
		}		
		return date;
	}
		
		if ( explicit_time != null ) {
			for ( int x = 0; x < explicit_time.size(); x++ ) {
				ParseLocation t = explicit_time.get(x);
				int start = t.getStart();
				int end = t.getEnd();
				int count = 0;
				String origString = command.substring(start, end);
				
				int dotIndex = origString.indexOf(".");
				while ( dotIndex != -1 ) {
					origString = origString.substring(0, dotIndex) + origString.substring(dotIndex+1) + command.charAt(end++);
					dotIndex = origString.indexOf(".");
					count++;
				}
				
				name2 = name2.substring(count);
			}
		}
		
		name1 = name1.trim();
		name2 = name2.trim();

		String[] words = splitStringWithWhitespace(name1);
		int lastWord = words.length - 1;
		if( hasKeyword(words[lastWord], KEYWORDS_DATETIME_SEPARATOR) ) {
			name1 = command.substring(0, command.lastIndexOf(words[lastWord])).trim();
		}

		String name = (name1 + " " + name2).trim();
		_logger.log(Level.INFO, "Final Task Name: " + name);
		return name;
	}
	
	private ICommand doAdd(String command) throws InvalidCommandException {
		if ( command.isEmpty() ) {
			throw new InvalidCommandException();
		}
		
		assert !command.isEmpty() : "Parameters are empty";
		List<DateGroup> groups = _natty.parse(command);
		AddCommand cmd = new AddCommand();
		
		if ( groups.size() > 0 ) {
			// Tasks with date and/or time in it - either deadline or timed
			DateGroup group = groups.get(0);
			List<Date> dates = group.getDates();
			
			String name = determineTaskName(command, group);
			if ( name.isEmpty() ) {
				throw new InvalidCommandException();
			}
			
			_logger.log(Level.INFO, "Adding Task: " + name + "\n" + "Date Info Detected: " + group.getText() + "\n" + "Date Info Parsed: " + dates + "\n" + "Is Date Time Inferred: " + group.isTimeInferred());

			LocalDateTime[] startAndEndDateTime = getStartAndEndDateTime(dates);

			cmd.setTaskName(name);
			
			if ( startAndEndDateTime[DATETIME_START] != null ) {
				cmd.setStartDate(startAndEndDateTime[DATETIME_START].toLocalDate());
				
				if ( group.isTimeInferred() ) {
					cmd.setStartTime(null);
				} else {
					cmd.setStartTime(startAndEndDateTime[DATETIME_START].toLocalTime());
				}
			}
			
			if ( startAndEndDateTime[DATETIME_END] != null ) {
				cmd.setEndDate(startAndEndDateTime[DATETIME_END].toLocalDate());
				if ( group.isTimeInferred() ) {
					cmd.setEndTime(null);
				} else {
					cmd.setEndTime(startAndEndDateTime[DATETIME_END].toLocalTime());
				}
			}
			
			_logger.log(Level.INFO, "Added {0} -- {1} to {2}", new Object[] { cmd.getTaskName(), (cmd.getStartDateTime() == null ? "null" : cmd.getStartDateTime()), (cmd.getEndDateTime() == null ? "null" : cmd.getEndDateTime()) });
		} else {
			// Tasks without any deadlines - floating
			String name = command.trim();
			
			cmd.setTaskName(name);
			_logger.log(Level.INFO, "Added {0} - No date and time set", cmd.getTaskName());
		}
		
		return cmd;
	}
	
	private ICommand doUpdate(String command) throws InvalidCommandException {
		if ( command.isEmpty() ) {
			throw new InvalidCommandException();
		}
		
		assert !command.isEmpty() : "Parameters are empty";
		
		int taskNumber = 0;
		try {
			taskNumber = Integer.parseInt(CommandParser.getCommandKeyword(command));
		} catch (NumberFormatException e) {
			throw new InvalidCommandException();
		}
		
		String query = CommandParser.getCommandParameters(command);
		
		UpdateCommand cmd = new UpdateCommand(taskNumber);
		List<DateGroup> groups = _natty.parse(query);
	
		if ( groups.size() > 0 ) {
			// Date and Time Specified
			DateGroup group = groups.get(0);
			List<Date> dates = group.getDates();

			String name = determineTaskName(query, group);
			if ( !name.isEmpty() ) {
				// updating title also
				cmd.setTaskTitleToUpdate(name);
			}

			_logger.log(Level.INFO, "Updating Task: " + name + "\n" + "Date Info Detected: " + group.getText() + "\n" + "Date Info Parsed: " + dates + "\n" + "Is Date Time Inferred: " + group.isTimeInferred());
			LocalDateTime[] startAndEndDateTime = getStartAndEndDateTime(dates);
			
			if ( startAndEndDateTime[DATETIME_START] != null ) {
				if ( group.getParseLocations().containsKey("date") ) {
					cmd.setStartDateToUpdate(startAndEndDateTime[DATETIME_START].toLocalDate());
				}
				
				if ( !group.isTimeInferred() ) {
					cmd.setStartTimeToUpdate(startAndEndDateTime[DATETIME_START].toLocalTime());
				}
			}
			
			if ( startAndEndDateTime[DATETIME_END] != null ) {
				if ( group.getParseLocations().containsKey("date") ) {
					cmd.setEndDateToUpdate(startAndEndDateTime[DATETIME_END].toLocalDate());
				}
				
				if ( !group.isTimeInferred() ) {
					cmd.setEndTimeToUpdate(startAndEndDateTime[DATETIME_END].toLocalTime());
				}
			}
		} else {
			// Changing Title Only
			cmd.setTaskTitleToUpdate(query);
		}

		_logger.log(Level.INFO, "Updated Task #{0}\n-- Title: {1}\n-- Start Date: {2}\n-- Start Time: {3}\n-- End Date: {4}\n-- End Time: {5}",  new Object[] {
				taskNumber,
				(cmd.getTaskTitleToUpdate() == null ? "null" : cmd.getTaskTitleToUpdate()),
				(cmd.getStartDateToUpdate() == null ? "null" : cmd.getStartDateToUpdate()),
				(cmd.getStartTimeToUpdate() == null ? "null" : cmd.getStartTimeToUpdate()),
				(cmd.getEndDateToUpdate() == null ? "null" : cmd.getEndDateToUpdate()),
				(cmd.getEndTimeToUpdate() == null ? "null" : cmd.getEndTimeToUpdate())
			});
		
		return cmd;
	}
	
	private ICommand doDelete(String command) throws InvalidCommandException {
		if ( command.isEmpty() ) {
			throw new InvalidCommandException();
		}
		
		assert !command.isEmpty() : "Parameters are empty";
		
		int itemNumber = 0;
		try {
			itemNumber = Integer.parseInt(CommandParser.getCommandKeyword(command));
		} catch (NumberFormatException e) {
			throw new InvalidCommandException();
		}
		
		_logger.log(Level.INFO, "Deleting Task: {0}", itemNumber);
		return new DeleteCommand(itemNumber);
	}
	
	private ICommand doView(String command) {
		String keywords;
		
		String keyword = CommandParser.getCommandKeyword(command);
		String query = CommandParser.getCommandParameters(command);

		ViewType viewType = getViewType(keyword);
		ViewCommand cmd = new ViewCommand(viewType);

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
				cmd.setSearchKeywords(keywords);
			} else {
				String[] words = splitStringWithWhitespace(keywords);
				int firstWord = 0;
				relativeType = getRelativeType(words[firstWord]);
				if( relativeType != RelativeType.NONE ) {
					keywords = keywords.substring(words[firstWord].length()).trim();
				}
				
				keywords = keywords.trim();
			}

			_logger.log(Level.INFO, "View Type: " + viewType + "\nRelative Type: " + relativeType + "\nKeywords: " + keywords + "\n" + "Date Info Detected: " + group.getText() + "\n" + "Date Info Parsed: " + dates + "\n" + "Is Date Time Inferred: " + group.isTimeInferred());
		
			if ( relativeType == RelativeType.BEFORE ) {
				cmd.setStartDateTime(MIN_DATETIME);
				cmd.setEndDateTime(startAndEndDateTime[DATETIME_END]);
			} else if ( relativeType == RelativeType.AFTER ) {
				cmd.setStartDateTime(startAndEndDateTime[DATETIME_END]);
				cmd.setEndDateTime(MAX_DATETIME);
			} else if ( relativeType == RelativeType.EXACT ) {
				cmd.setStartDateTime(startAndEndDateTime[DATETIME_START]);
				cmd.setEndDateTime(startAndEndDateTime[DATETIME_END]);
				cmd.setStartTime(null);
				cmd.setEndTime(null);
			} else if ( relativeType == RelativeType.SPECIFIED ) {
				cmd.setStartDateTime(startAndEndDateTime[DATETIME_START]);
				cmd.setEndDateTime(startAndEndDateTime[DATETIME_END]);
			}
			
			if ( group.isTimeInferred() ) {
				cmd.setStartTime(null);
				cmd.setEndTime(null);
			}
		} else {
			keywords = keywords.trim();
			cmd.setSearchKeywords(keywords);
			_logger.log(Level.INFO, "Searching for tasks with keywords: {0}", keywords);
		}
		
		return cmd;
	}

	private ICommand doUndo(String command) {
		int steps = 1;
		try {
			steps = Integer.parseInt(command);
			steps = (steps > 0) ? steps : 1;
		} catch ( NumberFormatException ex ) {
			steps = 1;
		}
		
		_logger.log(Level.INFO, "Undo (Steps: {0})", steps);
		return new UndoCommand(steps);
	}
	
	private ICommand doRedo(String command) {
		int steps = 1;
		try {
			steps = Integer.parseInt(command);
			steps = (steps > 0) ? steps : 1;
		} catch ( NumberFormatException ex ) {
			steps = 1;
		}
		
		_logger.log(Level.INFO, "Redo (Steps: {0})", steps);
		return new RedoCommand(steps);
	}
	
	private ICommand doMark(String command) throws InvalidCommandException {
		if ( command.isEmpty() ) {
			throw new InvalidCommandException();
		}
		
		assert !command.isEmpty() : "Parameters are empty";

		int itemNumber = 0;
		try {
			itemNumber = Integer.parseInt(CommandParser.getCommandKeyword(command));
		} catch (NumberFormatException e) {
			throw new InvalidCommandException();
		}

		_logger.log(Level.INFO, "Marking Task {0} as Complete", itemNumber);
		return new MarkCommand(itemNumber);
	}
	
	private ICommand doUnmark(String command) throws InvalidCommandException {
		if ( command.isEmpty() ) {
			throw new InvalidCommandException();
		}
		
		assert !command.isEmpty() : "Parameters are empty";

		int itemNumber = 0;
		try {
			itemNumber = Integer.parseInt(CommandParser.getCommandKeyword(command));
		} catch (NumberFormatException e) {
			throw new InvalidCommandException();
		}

		_logger.log(Level.INFO, "Unmarking Task {0} as Incomplete", itemNumber);
		return new UnmarkCommand(itemNumber);
	}

	private ICommand doDirectory() {
		_logger.log(Level.INFO, "Changing Directory");
		return new DirectoryCommand();
	}
	
	private ICommand doExit() {
		_logger.log(Level.INFO, "Exiting Taskie");
		return new ExitCommand();
	}
	
	private static LocalDateTime[] getStartAndEndDateTime(List<Date> dates) {
		LocalDateTime[] startAndEndDateTime = new LocalDateTime[NUM_START_END_DATETIME];

		if ( dates.size() > 1 ) {
			Date datetime1 = dates.get(0);
			Date datetime2 = dates.get(1);
			
			LocalDateTime ldt1 = convertDateToLocalDateTime(datetime1);
			LocalDateTime ldt2 = convertDateToLocalDateTime(datetime2);
			
			if ( ldt1.isAfter(ldt2) ) {
				startAndEndDateTime[DATETIME_START] = ldt2;
				startAndEndDateTime[DATETIME_END] = ldt1;
			} else {
				startAndEndDateTime[DATETIME_START] = ldt1;
				startAndEndDateTime[DATETIME_END] = ldt2;
			}
		} else if ( dates.size() == 1 ) {
			// Deadline tasks

			Date datetime1 = dates.get(0);
			LocalDateTime ldt1 = convertDateToLocalDateTime(datetime1);
			startAndEndDateTime[DATETIME_START] = null;
			startAndEndDateTime[DATETIME_END] = ldt1;
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
		return command.replaceFirst(getCommandKeyword(command), "").trim();
	}

	private static String[] splitStringWithWhitespace(String command) {
		return command.trim().split("\\s+");
	}

	private static LocalDateTime convertDateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
}
