package taskie.models;

import java.time.format.DateTimeFormatter;

//@author A0097582N
public class Messages {
	public static final String NEWLINE = System.lineSeparator();
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mma");
	public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy hh:mm:ssa");

	// UI Messages
	public static final String UI_WELCOME_MESSAGE = "Welcome to Taskie!";
	public static final String UI_REQUEST_INPUT = "Enter Command: ";
	public static final String UI_HEADER = "Your To-Do List";

	// AddCommand messages
	public static final String ADD_FLOATING = "%s added." + NEWLINE;
	public static final String ADD_DEADLINED = "%s added at %s" + NEWLINE;
	public static final String ADD_TIMED = "%s added from %s to %s" + NEWLINE;

	// DeleteCommand messages
	public static final String DELETE_TASK = "%s is deleted from list." + NEWLINE;
	public static final String DELETE_TASK_FIELD = "Following fields are deleted from %s:" + NEWLINE;

	// ExitCommand messages
	public static final String EXIT_MESSAGE = "Taskie is exiting. Hope to see you soon!" + NEWLINE;

	// MarkCommand messages
	public static final String MARK_STRING = "%s marked as done." + NEWLINE;
	public static final String TASK_ALREADY_DONE = "Task is already done."+ NEWLINE;
	
	// UnmarkCommand messages
	public static final String UNMARK_STRING = "%s marked as not done." + NEWLINE;
	public static final String TASK_ALREADY_NOT_DONE = "Task is not done."+NEWLINE;
	// UpdateCommand messages
	public static final String UPDATE_STRING = "Following task fields from %s is updated:" + NEWLINE;
	public static final String INVALID_DATE_INPUT = "Invalid Command. Date input is not valid. Please try again." + NEWLINE;
	public static final String TASK_TITLE = "Task Title" + NEWLINE;
	public static final String START_DATE = "Start date" + NEWLINE;
	public static final String START_TIME = "Start time" + NEWLINE;
	public static final String END_DATE = "End date" + NEWLINE;
	public static final String END_TIME = "End time" + NEWLINE;

	// DirectoryCommand messages
	public static final String DIRECTORY_CHANGED = "Directory changed to %s" + NEWLINE;
	public static final String DIRECTORY_NOT_CHANGED = "";
	public static final String DIRECTORY_INVALID = "The directory you selected is not valid." + NEWLINE;
	public static final String DIRECTORY_FILE_EXISTS = "The directory you selected contains an existing Taskie Database." + NEWLINE;
	public static final String DIRECTORY_CHANGE_FAILED = "Directory change failed." + NEWLINE;

	// help messages
	public static final String ADD_HELP = "HELP: Add a New Task " + NEWLINE 
			+ "add <task title> "
			+ "[from/on/between/by/in/at/on <start date/time>] "
			+ "[to/till <end date/time>]" + NEWLINE;
	public static final String VIEW_HELP = "HELP: View and Search Existing Tasks " + NEWLINE
			+ "view [all/undone/done/due]" + NEWLINE;
	public static final String UPDATE_HELP = "HELP: Update an Existing Task " + NEWLINE
			+ "update <task number> [<task title>] "
			+ "[from <start date/time> to <end date/time> / by <end date/time>]" + NEWLINE;
	public static final String DELETE_HELP = "HELP: Delete an Existing Task " + NEWLINE 
			+ "delete <task number>" + NEWLINE;
	public static final String UNDO_HELP = "HELP: Undo One or More Actions " + NEWLINE 
			+ "undo [<number of steps>]" + NEWLINE;
	public static final String REDO_HELP = "HELP: Redo One or More Actions " + NEWLINE 
			+ "redo [<number of steps>]" + NEWLINE;
	public static final String MARK_HELP = "HELP: Mark a Task as Done " + NEWLINE 
			+ "mark <task number>" + NEWLINE;
	public static final String UNMARK_HELP = "HELP: Mark a Task as Undone " + NEWLINE
			+ "unmark <task number>" + NEWLINE;
	public static final String DIRECTORY_HELP = "HELP: Change Storage Directory " + NEWLINE
			+ "directory <new path>" + NEWLINE;
	public static final String EXIT_HELP = "HELP: Exit Taskie " + NEWLINE 
			+ "exit" + NEWLINE;
	
	// error messages
	public static final String INVALID_TASK_NUM = "Invalid task number. Please try again." + NEWLINE;
	public static final String INVALID_COMMAND = "Invalid Command. Please try again." + NEWLINE;

	// UndoCommand messages
	public static final String NOTHING_TO_UNDO = "Nothing to undo." + NEWLINE;
	public static final String UNDO_FAILED = "Undo failed." + NEWLINE;

	// RedoCommand messages
	public static final String NOTHING_TO_REDO = "Nothing to redo." + NEWLINE;
	public static final String REDO_FAILED = "Redo failed." + NEWLINE;
	

}