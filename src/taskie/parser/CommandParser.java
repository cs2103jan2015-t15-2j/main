package taskie.parser;

import java.util.Arrays;

public class CommandParser implements Parser {
	private static String[] KEYWORDS_UPDATE = new String[] {"update", "change", "modify"};
	private static String[] KEYWORDS_VIEW = new String[] {"view", "list", "show", "open"};
	private static String[] KEYWORDS_DELETE = new String[] {"delete", "clear", "remove"};
	private static String[] KEYWORDS_SEARCH = new String[] {"search", "find", "look"};
	private static String[] KEYWORDS_UNDO = new String[] {"undo", "revert"};
	private static String[] KEYWORDS_EXIT = new String[] {"exit", "quit", "close"};
	
	private enum CommandType {
		ADD, UPDATE, VIEW, DELETE, SEARCH, UNDO, EXIT
	};
	
	private String command;
	
	public void parse(String command) {
		this.command = command;
		
		String keyword = CommandParser.getCommandKeyword(command);
		CommandType cmd = this.getCommandType(keyword);
		this.executeCommandType(cmd);
	}
	
	private CommandType getCommandType(String key) {
		if ( hasKeyword(key, CommandParser.KEYWORDS_UPDATE) ) {
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
			return CommandType.ADD;
		}
	}
	
	private void executeCommandType(CommandType cmd) {
		if ( cmd == CommandType.ADD ) {
			this.doAdd();
		} else if ( cmd == CommandType.UPDATE ) {
			this.doUpdate();
		} else if ( cmd == CommandType.VIEW ) {
			this.doView();
		} else if ( cmd == CommandType.DELETE ) {
			this.doDelete();
		} else if ( cmd == CommandType.SEARCH ) {
			this.doSearch();
		} else if ( cmd == CommandType.EXIT ) {
			this.doExit();
		}
	}
	
	private void doAdd() {
		NattyParser _natty = new NattyParser();
		_natty.parse(command);
	}
	
	private void doUpdate() {
		NattyParser _natty = new NattyParser();
		_natty.parse(command);
	}
	
	private void doView() {
		
	}

	private void doDelete() {
		
	}
	
	private void doSearch() {
		
	}
	
	private void doExit() {
		
	}
	
	private static boolean hasKeyword(String needle, String[] haystack) {
		return (Arrays.asList(haystack).contains(needle));
	}
	
	private static String getCommandKeyword(String command) {
		return command.trim().split("\\s+")[0];	
	}
}
