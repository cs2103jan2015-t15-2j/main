//@author A0121555M
package taskie.ui;

import taskie.models.Task;

public interface UI {
	public void run();
	public String readInput();
	public void display(Task task);
	public void display(Task[] task);
	public void display(String message);
	public void exit();
	public Task[] getCurrentTaskList();
	public boolean isUIRunning();
}
