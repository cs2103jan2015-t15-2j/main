package taskie.models;

import java.time.format.DateTimeFormatter;

//@author A0097582N
public class Messages {
	public static final String NEWLINE = System.lineSeparator();
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy");
	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mma");
	public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy hh:mma");

	// UI Messages
	public static final String UI_WELCOME_MESSAGE = "Welcome to Taskie!";
	public static final String UI_REQUEST_INPUT = "Enter Command: ";
	public static final String UI_HEADER = "Your To-Do List";
	public static final String UI_NO_TASKS = "No tasks found!";

	// AddCommand messages
	public static final String ADD_FLOATING = "%s added." + NEWLINE;
	public static final String ADD_DEADLINED = "%s added at %s" + NEWLINE;
	public static final String ADD_TIMED = "%s added from %s to %s" + NEWLINE;
	public static final String ADD_CONFLICT = "[!!!] Warning! This task conflicts with: ";

	// DeleteCommand messages
	public static final String DELETE_TASK = "%s has been deleted." + NEWLINE;
	public static final String DELETE_TASK_FIELD = "The following fields have been deleted from %s: %s" + NEWLINE;
	public static final String START_DATE_STRING = "start date, ";
	public static final String START_TIME_STRING = "start time, ";
	public static final String END_DATE_STRING = "end date, ";
	public static final String END_TIME_STRING = "end time, ";

	// ExitCommand messages
	public static final String EXIT_MESSAGE = "Taskie is exiting. Hope to see you again!";

	// MarkCommand messages
	public static final String MARK_STRING = "%s marked as complete." + NEWLINE;
	public static final String TASK_ALREADY_DONE = "%s is already complete." + NEWLINE;

	// UnmarkCommand messages
	public static final String UNMARK_STRING = "%s marked as incomplete" + NEWLINE;
	public static final String TASK_ALREADY_NOT_DONE = "%s is already incomplete." + NEWLINE;

	// UpdateCommand messages
	public static final String UPDATE_STRING ="%s has been updated as follows:" + NEWLINE;
	public static final String INVALID_DATE_INPUT = "Invalid Command. Date input is not valid. Please try again." + NEWLINE;
	public static final String TASK_TITLE = "Task Title has changed from %s to %s" + NEWLINE;
	public static final String START_DATE_TIME = "Start date and time changed from %s to %s" + NEWLINE;
	public static final String END_DATE_TIME = "End date and time changed from %s to %s" + NEWLINE;
	public static final String START_DATE_TIME_NULL = "Task start date updated to %s." + NEWLINE;
	public static final String END_DATE_TIME_NULL = "Task end date updated to %s." +NEWLINE;
	public static final String UPDATE_START_DATE_REMOVED = "Task start date has been removed." + NEWLINE;
	public static final String UPDATE_END_DATE_REMOVED = "Task end date has been removed." +NEWLINE;
	public static final Object UPDATE_TASK_DATE_INVALID = "Task Date is Invalid"+ NEWLINE;

	// DirectoryCommand messages
	public static final String DIRECTORY_CHANGED = "Directory changed to %s" + NEWLINE;
	public static final String DIRECTORY_NOT_CHANGED = "Directory has not been changed." + NEWLINE;
	public static final String DIRECTORY_INVALID = "The directory you selected is not valid." + NEWLINE;
	public static final String DIRECTORY_FILE_EXISTS = "The directory you selected contains an existing Taskie Database." + NEWLINE;
	public static final String DIRECTORY_CHANGE_FAILED = "Directory change failed." + NEWLINE;

	// help messages
	public static final String ADD_HELP_HEADER = "HELP: Add a New Task " + NEWLINE;
	public static final String ADD_HELP_BODY = "add <task title> " + "[from/on/between/by/in/at/on <start date/time>] " + "[to/till <end date/time>]" + NEWLINE + NEWLINE;

	public static final String VIEW_HELP_HEADER = "HELP: View and Search Existing Tasks" + NEWLINE;
	public static final String VIEW_HELP_BODY = "view [all/undone/done/due]" + NEWLINE + NEWLINE;

	public static final String UPDATE_HELP_HEADER = "HELP: Update an Existing Task" + NEWLINE;
	public static final String UPDATE_HELP_BODY = "update <task number> [<task title>] " + "[from <start date/time> to <end date/time> / by <end date/time>]" + NEWLINE + NEWLINE;

	public static final String DELETE_HELP_HEADER = "HELP: Delete an Existing Task" + NEWLINE;
	public static final String DELETE_HELP_BODY = "delete <task number>" + NEWLINE + NEWLINE;

	public static final String UNDO_HELP_HEADER = "HELP: Undo One or More Actions" + NEWLINE;
	public static final String UNDO_HELP_BODY = "undo [<number of steps>]" + NEWLINE + NEWLINE;

	public static final String REDO_HELP_HEADER = "HELP: Redo One or More Actions" + NEWLINE;
	public static final String REDO_HELP_BODY = "redo [<number of steps>]" + NEWLINE + NEWLINE;

	public static final String MARK_HELP_HEADER = "HELP: Mark a Task as Done" + NEWLINE;
	public static final String MARK_HELP_BODY = "mark <task number>" + NEWLINE + NEWLINE;

	public static final String UNMARK_HELP_HEADER = "HELP: Mark a Task as Undone" + NEWLINE;
	public static final String UNMARK_HELP_BODY = "unmark <task number>" + NEWLINE + NEWLINE;

	public static final String DIRECTORY_HELP_HEADER = "HELP: Change Storage Directory" + NEWLINE;
	public static final String DIRECTORY_HELP_BODY = "directory <new path>" + NEWLINE + NEWLINE;

	public static final String EXIT_HELP_HEADER = "HELP: Exit Taskie" + NEWLINE;
	public static final String EXIT_HELP_BODY = "exit" + NEWLINE + NEWLINE;

	// error messages
	public static final String INVALID_TASK_NUM = "Invalid task number. Please try again." + NEWLINE;
	public static final String INVALID_COMMAND = "Invalid Command. Please try again." + NEWLINE;

	// UndoCommand messages
	public static final String NOTHING_TO_UNDO = "Nothing to undo." + NEWLINE;
	public static final String UNDO_FAILED = "Undo failed: %s" + NEWLINE;

	// RedoCommand messages
	public static final String NOTHING_TO_REDO = "Nothing to redo." + NEWLINE;
	public static final String REDO_FAILED = "Redo failed: %s" + NEWLINE;

	// Exception messages
	public static final String CONFIGURATION_FAILED_EXCEPTION = "The configuration could not be settled." + NEWLINE;
	public static final String FILE_EXISTS_EXCEPTION = "A file already exists at this directory." + NEWLINE;
	public static final String INVALID_COMMAND_EXCEPTION = "The command could not be parsed." + NEWLINE;
	public static final String INVALID_TASK_EXCEPTION = "The task is not valid" + NEWLINE;
	public static final String NOTHING_TO_REDO_EXCEPTION = "There is nothing to redo." + NEWLINE;
	public static final String STORAGE_LOCATION_INVALID_EXCEPTION = "Storage Location is invalid" + NEWLINE;
	public static final String STORAGE_MIGRATION_FAILED_EXCEPTION = "Storage Location is invalid" + NEWLINE;
	public static final String TASK_DATE_INVALID_EXCEPTION = "Invalid date and time set in task" + NEWLINE;
	public static final String TASK_DATE_NOT_SET_EXCEPTION = "A date has not been set for the task." + NEWLINE;
	public static final String TASK_MODIFICATION_FAILED_EXCEPTION = "Task modification failed" + NEWLINE;
	public static final String TASK_RETRIEVAL_FAILED_EXCEPTION = "Failed to retrieve task(s)" + NEWLINE;
	public static final String TASK_TYPE_NOT_SUPPORTED_EXCEPTION = "This task type is not supported." + NEWLINE;
	public static final String UNDO_NOT_SUPPORTED_EXCEPTION = "Undo is not supported for this command" + NEWLINE;
	public static final String SECURITY_EXCEPTION = "SecurityException: Unable to setup logging." + NEWLINE;
	public static final String IO_EXCEPTION = "IOException: Unable to setup logging." + NEWLINE;
	public static final String UNKNOWN_EXCEPTION = "Unknown Error: ";
	public static final String STORAGE_INITIALISATION_ERROR = "Critital: Unable to initialize Storage System";
}