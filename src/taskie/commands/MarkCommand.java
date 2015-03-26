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
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
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
	
	
	public void execute(){
		try{
			_task = retrieveTaskFromUI();
			Task updatedTask=new Task(_task);
			
			if(_task.getTaskStatus()){
				Taskie.Controller.getUI().display("stub. Task already done");
			}else{
				updatedTask.setTaskDone();
				Taskie.Controller.getUI().display(formatMarkString());
			}
			try {
				Taskie.Controller.getStorage().updateTask(_task, updatedTask);
			} catch (TaskTypeNotSupportedException e) {
				Taskie.Controller.getUI().display(e.getMessage());
			} catch (TaskModificationFailedException e) {
				Taskie.Controller.getUI().display(e.getMessage());
			}
		}catch(InvalidTaskException e){
			Taskie.Controller.getUI().display(taskie.models.Messages.INVALID_TASK_NUM);
		}
	}
/*	//@author A0097582N-unused
 * reason for unused: new APIs used deprecated this method.
	public void execute() {
		try {
			_task = retrieveTaskFromParser();
			HashMap<TaskType, ArrayList<Task>> taskLists = Taskie.Controller.getStorage().retrieveTaskMap();
			TaskType taskType = _task.getTaskType();
			scanListForTaskAndMark(_task, taskLists, taskType);
		} catch (InvalidTaskException e) {
			Taskie.Controller.getUI().display(taskie.models.Messages.INVALID_TASK_NUM);
		} catch (TaskRetrievalFailedException e) {
			Taskie.Controller.getUI().display(e.getMessage());
		}
	}

	private void scanListForTaskAndMark(Task task, HashMap<TaskType, ArrayList<Task>> taskLists, TaskType taskType) {
		try {
			ArrayList<Task> taskList = taskLists.get(taskType);
			int taskIndex = taskList.indexOf(task);
			Task taskRetrieved = taskList.get(taskIndex);
			taskRetrieved.setTaskDone();
			Taskie.Controller.getStorage().storeTaskMap(taskLists);
			Taskie.Controller.getUI().display(formatMarkString());
		} catch (TaskTypeNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TaskModificationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/
	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING,
				_task.getTitle());
	}

	private Task retrieveTaskFromUI() throws InvalidTaskException {
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
