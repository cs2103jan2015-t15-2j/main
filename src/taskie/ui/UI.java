//@author A0121555M
package taskie.ui;

import taskie.exceptions.InvalidTaskException;
import taskie.models.Task;

public interface UI {
	public void run();
	public String readInput();
	public void display(Task task);
	public void display(Task[] task);
	public void display(String message);
	public void exit();
	public Task[] getCurrentTaskList() throws InvalidTaskException;
	public Task getTask(int index) throws InvalidTaskException;
	public boolean isUIRunning();
}
