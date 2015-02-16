package taskie.parser;

import java.util.Arrays;

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
	
	private enum CommandType {
		ADD, UPDATE, VIEW, DELETE, SEARCH, UNDO, EXIT, INVALID
	};
	
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
			UI cmdUI = new CommandUI();
			cmdUI.display("Invalid Command");
		}
	}
	
	private void doAdd(String command) {
		NattyParser _natty = new NattyParser();
		_natty.parse(command);
	}
	
	private void doUpdate(String command) {
		NattyParser _natty = new NattyParser();
		_natty.parse(command);
	}
	
	private void doView(String command) {
		
	}

	private void doDelete(String command) {
		
	}
	
	private void doSearch(String command) {
		
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
		return command.replace(getCommandKeyword(command) + " ", "");
	}
}
