//@author A0121555M
package taskie.ui;

import java.time.LocalDateTime;

import taskie.exceptions.InvalidTaskException;
import taskie.models.DisplayType;
import taskie.models.Task;

public interface UI {	
	public void run();

	public String readInput();

	public void display(Task task);

	public void display(Task[] task);

	public void display(DisplayType type, String message);

	public void exit();

	public Task[] getCurrentTaskList() throws InvalidTaskException;

	public Task getTask(int index) throws InvalidTaskException;

	public boolean isUIRunning();
	
	public String formatDateTime(LocalDateTime dateTime);
	
	public String formatDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime);

	public String loadSelectDirectoryDialog(String currentDirectory);
}
