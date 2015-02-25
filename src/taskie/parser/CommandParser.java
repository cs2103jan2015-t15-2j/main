//@author A0121555M
package taskie.parser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import taskie.Taskie;
import taskie.commands.AddCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.models.CommandType;
import taskie.models.Task;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.ParseLocation;

public class CommandParser implements Parser {
	private static String MESSAGE_INVALID_COMMAND = "Invalid Command";
	
	private static String[] DATETIME_KEYWORDS = new String[] { "from", "on", "between", "by", "in", "at", "on" };
	
	private static String[] COMMAND_KEYWORD_ADD = new String[] {"add", "create", "new", "ins", "insert", "put"};
	private static String[] COMMAND_KEYWORD_UPDATE = new String[] {"update", "change", "modify", "edit", "alter"};
	private static String[] COMMAND_KEYWORD_DELETE = new String[] {"delete", "clear", "remove"};
	private static String[] COMMAND_KEYWORD_VIEW_AND_SEARCH = new String[] {"search", "find", "look", "display", "show", "open", "view", "list"};
	private static String[] COMMAND_KEYWORD_UNDO = new String[] {"undo", "revert"};
	private static String[] COMMAND_KEYWORD_EXIT = new String[] {"exit", "quit", "close"};
	
	private com.joestelmach.natty.Parser _natty;
	
	public CommandParser() {
		_natty = new com.joestelmach.natty.Parser();
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
		if ( command.trim().isEmpty() ) {
			Taskie.UI.display(MESSAGE_INVALID_COMMAND);
			return;
		}
		
		List<DateGroup> groups = _natty.parse(command);
		
		if ( groups.size() > 0 ) {
			for ( DateGroup group : groups ) {
				String name = command.substring(0, group.getPosition()).trim();
				String description = command.substring(group.getPosition() + group.getText().length()).trim();
				
				System.out.println(name);
				
				List<Date> dates = group.getDates();
				for ( Date date : dates ) {
	                  System.out.println(group.getSyntaxTree().toStringTree());
	                  System.out.println("line: " + group.getLine() + ", column: " + group.getPosition());
	                  System.out.println(group.getText());
	                  System.out.println(group.getDates());
	                  
	                  System.out.println("\n** Parse Locations **");
	                  for(Entry<String, List<ParseLocation>> entry:group.getParseLocations().entrySet()) {
	                    for(ParseLocation loc:entry.getValue()) {
	                      System.out.println(loc.getRuleName() + ": " + loc.getText());
	                    }
	                  }
	                  
	                  List<ParseLocation> conjunctionLocations = group.getParseLocations().get("conjunction");
	                  if(conjunctionLocations != null) {
	                    System.out.print("\nconjunctions: ");
	                    for(ParseLocation location:conjunctionLocations) {
	                      System.out.print(location.getText() + " ");
	                    }
	                  }
	                  System.out.print("\n\n");

	                  
					if ( group.isTimeInferred() ) {
						Task task = new Task(name);
						Taskie.Controller.executeCommand(new AddCommand(task));

						Taskie.UI.display("Added " + name + " -- " + date.toString());
					} else {
						Task task = new Task(name);
						Taskie.Controller.executeCommand(new AddCommand(task));

						Taskie.UI.display("Added " + name + " -- " + date.toString());
					}
				}
			}
		} else {
			// Tasks without any deadlines
			String name = command.trim();
			
			Task task = new Task(name);
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
		return (Arrays.asList(haystack).contains(needle));
	}
	
	private static String getCommandKeyword(String command) {
		return command.trim().split("\\s+")[0];	
	}
	
	private static String getCommandParameters(String command) {
		return command.replace(getCommandKeyword(command) + " ", "").trim();
	}
}
