/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.commands;

import java.util.ArrayList;
import java.util.HashMap;

import taskie.Taskie;
import taskie.database.Storage;
import taskie.models.CommandType;
import taskie.models.Task;

public class MarkCommand implements ICommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;

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
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Storage
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
		Taskie.Storage.storeTaskMap(taskLists);
	}

}
