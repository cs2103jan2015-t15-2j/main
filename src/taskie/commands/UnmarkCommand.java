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
import taskie.exceptions.InvalidTaskException;
import taskie.models.CommandType;
import taskie.models.Task;

public class UnmarkCommand implements ICommand {

	private CommandType _commandType = CommandType.MARK;
	private int _taskIndex;
	private Task _task;

	//@author A0121555M
	public UnmarkCommand(int itemNumber) {
		_taskIndex = itemNumber;
	}

	//@author A0097582N
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
		try {
			_task = retrieveTaskFromParser();
			HashMap<String, ArrayList<Task>> taskLists = Taskie.Controller
					.getStorage().retrieveTaskMap();
			String taskType = Taskie.Controller.determineTaskType(_task);
			scanListForTaskAndUnmark(_task, taskLists, taskType);
			Taskie.Controller.getUI().display(formatUnmarkString());
		} catch (InvalidTaskException e) {
			Taskie.Controller.getUI().display(
					taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private void scanListForTaskAndUnmark(Task task,
			HashMap<String, ArrayList<Task>> taskLists, String taskType) {
		ArrayList<Task> taskList = taskLists.get(taskType);
		int taskIndex = taskList.indexOf(task);
		Task taskRetrieved = taskList.get(taskIndex);
		taskRetrieved.setTaskUndone();
		Taskie.Controller.getStorage().storeTaskMap(taskLists);
	}

	private String formatUnmarkString() {
		return String.format(taskie.models.Messages.UNMARK_STRING,
				_task.getTitle());
	}

	private Task retrieveTaskFromParser() throws InvalidTaskException {

		Task task = Taskie.Controller.getUI().getTask(_taskIndex);
		return task;
	}

	//@author A0121555M
	@Override	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex);
		return sb.toString();
	}
}
