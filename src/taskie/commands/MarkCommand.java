/**
 * class representing a update command. 
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

public class MarkCommand implements ICommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		Task[] tasks = Taskie.Controller.getUI().getCurrentTaskList();
		try {
			_task = tasks[itemNumber];
		} catch ( ArrayIndexOutOfBoundsException ex ) {
			Taskie.Controller.getUI().display("Invalid Task Number");
		}
	}
	
	//@author A0097582N
	public MarkCommand(Task task) {
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
		scanListForTaskAndMark(_task, taskLists,taskType);

	}

	private void scanListForTaskAndMark(Task task,
			HashMap<String, ArrayList<Task>> taskLists,String taskType) {
		ArrayList<Task> taskList=taskLists.get(taskType);
		int taskIndex=taskList.indexOf(task);
		Task taskRetrieved=taskList.get(taskIndex);
		taskRetrieved.setTaskDone();
		Taskie.Controller.getStorage().storeTaskMap(taskLists);
		Taskie.Controller.getUI().display("Mark Task. (STUB). Task title: %s");
	}

}
