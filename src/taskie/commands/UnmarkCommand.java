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
		HashMap<String, ArrayList<Task>> taskLists = Taskie.Storage
				.retrieveTaskMap();
		String taskType = Taskie.Controller.determineTaskType(_task);
		scanListForTaskAndUnmark(_task, taskLists,taskType);
		//TODO add message return to user

	}

	private void scanListForTaskAndUnmark(Task task,
			HashMap<String, ArrayList<Task>> taskLists,String taskType) {
		ArrayList<Task> taskList=taskLists.get(taskType);
		int taskIndex=taskList.indexOf(task);
		Task taskRetrieved=taskList.get(taskIndex);
		taskRetrieved.setTaskUndone();
		Taskie.Storage.storeTaskMap(taskLists);
	}

}
