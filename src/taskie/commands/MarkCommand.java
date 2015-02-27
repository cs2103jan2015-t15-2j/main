package taskie.commands;

import java.util.ArrayList;
import java.util.HashMap;

import taskie.Taskie;
import taskie.database.Storage;
import taskie.models.CommandType;
import taskie.models.Task;

public class MarkCommand implements ICommand {

	private static final String DEADLINED_TASKNAME = "deadlined";
	private static final String TIMED_TASKNAME = "timed";
	private static final String FLOATING_TASKNAME = "floating";

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
		String taskType = determineTaskType(_task);
		scanListForTaskAndMark(_task, taskLists,taskType);

	}

	private void scanListForTaskAndMark(Task task,
			HashMap<String, ArrayList<Task>> taskLists,String taskType) {
		ArrayList<Task> taskList=taskLists.get(taskType);
		int taskIndex=taskList.indexOf(task);
		Task taskRetrieved=taskList.get(taskIndex);
		taskRetrieved.setTaskDone();
		Taskie.Storage.storeTaskList(taskLists);
	}

	private String determineTaskType(Task task) {
		if (task.getStartDate() == null && task.getEndDate() == null) {
			return FLOATING_TASKNAME;
		} else if (task.getStartDate() == null && task.getEndDate() != null) {
			return DEADLINED_TASKNAME;
		} else {
			return TIMED_TASKNAME;
		}
	}

}
