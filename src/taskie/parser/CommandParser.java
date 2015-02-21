//@author A0121555M
package taskie.parser;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import taskie.models.CommandType;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.ParseLocation;

import taskie.Taskie;
import taskie.ui.CommandUI;
import taskie.ui.UI;

public class CommandParser implements Parser {
	private static String[] KEYWORDS_ADD = new String[] {"add", "create"};
	private static String[] KEYWORDS_UPDATE = new String[] {"update", "change", "modify"};
	private static String[] KEYWORDS_VIEW = new String[] {"view", "list", "show", "open"};
	private static String[] KEYWORDS_DELETE = new String[] {"delete", "clear", "remove"};
	private static String[] KEYWORDS_SEARCH = new String[] {"search", "find", "look"};
	private static String[] KEYWORDS_UNDO = new String[] {"undo", "revert"};
	private static String[] KEYWORDS_EXIT = new String[] {"exit", "quit", "close"};
	
	private com.joestelmach.natty.Parser _natty;
	
	public CommandParser() {
		_natty = new com.joestelmach.natty.Parser();	
	}

	public void parse(String input) {
		String keyword = CommandParser.getCommandKeyword(input);
		String command = CommandParser.getCommandParameters(input);
		
		CommandType cmd = this.getCommandType(keyword);
		this.executeCommandType(cmd, command);
	}
	
	private CommandType getCommandType(String key) {
		if ( hasKeyword(key, CommandParser.KEYWORDS_ADD) ) {
			return CommandType.ADD;
		} else if ( hasKeyword(key, CommandParser.KEYWORDS_UPDATE) ) {
			return CommandType.UPDATE;
		} else if ( hasKeyword(key, CommandParser.KEYWORDS_VIEW) ) {
			return CommandType.VIEW;
		} else if ( hasKeyword(key, CommandParser.KEYWORDS_DELETE) ) {
			return CommandType.DELETE;
		} else if ( hasKeyword(key, CommandParser.KEYWORDS_SEARCH) ) {
			return CommandType.SEARCH;
		} else if ( hasKeyword(key, CommandParser.KEYWORDS_UNDO) ) {
			return CommandType.UNDO;
		} else if ( hasKeyword(key, CommandParser.KEYWORDS_EXIT) ) {
			return CommandType.EXIT;
		} else {
			return CommandType.INVALID;
		}
	}
	
	private void executeCommandType(CommandType cmd, String command) {
		if ( cmd == CommandType.ADD ) {
			this.doAdd(command);
		} else if ( cmd == CommandType.UPDATE ) {
			this.doUpdate(command);
		} else if ( cmd == CommandType.VIEW ) {
			this.doView(command);
		} else if ( cmd == CommandType.DELETE ) {
			this.doDelete(command);
		} else if ( cmd == CommandType.SEARCH ) {
			this.doSearch(command);
		} else if ( cmd == CommandType.UNDO ) {
			this.doUndo(command);
		} else if ( cmd == CommandType.EXIT ) {
			this.doExit();
		} else {
			Taskie.UI.display("Invalid Command");
		}
	}
	
	private void doAdd(String command) {
		if ( command.trim().isEmpty() ) {
			Taskie.UI.display("Invalid Command");
			return;
		}
		
		List<DateGroup> groups = _natty.parse(command);
		if ( groups.size() > 0 ) {
			for ( DateGroup group : groups ) {
			
				
				String name = command.substring(0, group.getPosition()).trim();
				String description = command.substring(group.getPosition() + group.getText().length()).trim();
				
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
			      
				List<Date> dates = group.getDates();
				for ( Date date : dates ) {
					if ( group.isTimeInferred() ) {
						// set time to end of day
					}
					Taskie.UI.display("Added " + name + " on " + date.toString());
				}
			}
		} else {
			// Tasks without any deadlines
			Taskie.UI.display("Added " + command);
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
	
	private void doView(String command) {
		
	}

	private void doDelete(String command) {
		
	}
	
	private void doSearch(String command) {
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
