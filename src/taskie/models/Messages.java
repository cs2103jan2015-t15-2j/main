package taskie.models;

public class Messages {
	// AddCommand messages
	public static final String ADD_FLOATING = "%s added.";
	public static final String ADD_DEADLINED = "%s added at %s";
	public static final String ADD_TIMED = "%s added from %s to %s";
	// DeleteCommand messages
	public static final String DELETE_TASK = "%s is deleted from list.";
	public static final String DELETE_TASK_FIELD = "Following fields are deleted from %s:";
	// ExitCommand messages
	public static final String EXIT_MESSAGE = "Taskie is exiting. Hope to see you soon!";
	// MarkCommand messages
	public static final String MARK_STRING = "%s marked as done.";
	// UnmarkCommand messages
	public static final String UNMARK_STRING = "%s marked as not done.";
	// UpdateCommand messages
	public static final String UPDATE_STRING = "Following task fields from %s is updated:";

	// error messages
	public static final String INVALID_TASK_NUM = "Invalid task number. Please try again.";
	public static final String INVALID_COMMAND = "Invalid Command. Please try again.";
}