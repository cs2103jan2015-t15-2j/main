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
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.Task;
import taskie.models.TaskType;

public class MarkCommand implements ICommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;
	private int _taskIndex;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndex = itemNumber;
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
		try {
			_task = retrieveTaskFromParser();
			HashMap<TaskType, ArrayList<Task>> taskLists = Taskie.Controller.getStorage().retrieveTaskMap();
			TaskType taskType = _task.getTaskType();
			scanListForTaskAndMark(_task, taskLists, taskType);
		} catch (InvalidTaskException e) {
			Taskie.Controller.getUI().display(
					taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private void scanListForTaskAndMark(Task task, HashMap<TaskType, ArrayList<Task>> taskLists, TaskType taskType) {
		ArrayList<Task> taskList = taskLists.get(taskType);
		int taskIndex = taskList.indexOf(task);
		Task taskRetrieved = taskList.get(taskIndex);
		taskRetrieved.setTaskDone();
		Taskie.Controller.getStorage().storeTaskMap(taskLists);
		Taskie.Controller.getUI().display(formatMarkString());
	}

	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING,
				_task.getTitle());
	}

	private Task retrieveTaskFromParser() throws InvalidTaskException {
		Task task = Taskie.Controller.getUI().getTask(_taskIndex);
		return task;
	}
	
	//@author A0121555M
	public void undo() {
		new UnmarkCommand(_taskIndex).execute();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + _taskIndex);
		return sb.toString();
	}
}
