/**
 * class representing an unmark command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import java.util.ArrayList;
import java.util.HashMap;

import taskie.Taskie;
import taskie.models.CommandType;
import taskie.models.Task;

public class UnmarkCommand implements ICommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;

	public UnmarkCommand(Task task) {
		_task = task;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public Task getTask() {
		return _task;
	}

	@Override
	public void execute() {
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller.getStorage()
				.retrieveTaskMap();
		String taskType = Taskie.Controller.determineTaskType(_task);
		scanListForTaskAndUnmark(_task, taskLists,taskType);
		Taskie.Controller.getUI().display("UNMARK TASK.(STUB) Task Title: %s");

	}

	private void scanListForTaskAndUnmark(Task task,
			HashMap<String, ArrayList<Task>> taskLists,String taskType) {
		ArrayList<Task> taskList=taskLists.get(taskType);
		int taskIndex=taskList.indexOf(task);
		Task taskRetrieved=taskList.get(taskIndex);
		taskRetrieved.setTaskUndone();
		Taskie.Controller.getStorage().storeTaskMap(taskLists);
	}

}
