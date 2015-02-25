//@author A0121555M
package taskie.ui;

import taskie.models.Task;

public interface UI {
	public void run();
	public boolean readInput();
	public void display(Task task);
	public void display(Task[] task);
	public void display(String message);
	public Task[] getCurrentTaskList();
}
