package taskie.models;

public class Messages {
	public static final String NEWLINE = System.lineSeparator();
	
	// UI Messages
	public static final String UI_WELCOME_MESSAGE = "Welcome to Taskie!" + NEWLINE;
	public static final String UI_REQUEST_INPUT = "Enter Command: ";
	
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
	
	// UnmarkCommand messages
	public static final String UNMARK_STRING = "%s marked as not done." + NEWLINE;
	
	// UpdateCommand messages
	public static final String UPDATE_STRING = "Following task fields from %s is updated:" + NEWLINE;

	// error messages
	public static final String INVALID_TASK_NUM = "Invalid task number. Please try again." + NEWLINE;
	public static final String INVALID_COMMAND = "Invalid Command. Please try again." + NEWLINE;
}