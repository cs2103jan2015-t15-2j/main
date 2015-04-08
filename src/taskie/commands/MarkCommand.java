/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Task;

public class MarkCommand extends AbstractCommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;
	private ArrayList<Integer> _taskIndexes;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndexes=new ArrayList<Integer>();
		_taskIndexes.add(itemNumber);
	}
	

	//@author A0097582N
	public MarkCommand(int[] taskIndexes){
		_taskIndexes = new ArrayList<Integer>();
		for(int i=0;i<taskIndexes.length;i++){
			_taskIndexes.add(taskIndexes[i]);
		}
	}
	
	public MarkCommand(Task task) {
		_task = task;
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public Task getTask() {
		return _task;
	}

	public void execute() {
		try {
			while(_taskIndexes.size()>0){
				_task = retrieveTaskFromUI();
				Task updatedTask = new Task(_task);
	
				if (_task.isDone()) {
					_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.TASK_ALREADY_DONE);
				} else {
					updatedTask.setTaskDone();
				}
				try {
					_controller.getStorage().updateTask(_task, updatedTask);
					_controller.getUI().display(DisplayType.SUCCESS, formatMarkString());
				} catch (TaskTypeNotSupportedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				} catch (TaskModificationFailedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				}
			}
		} catch (InvalidTaskException e) {
				_controller.getUI().display(DisplayType.ERROR,taskie.models.Messages.INVALID_TASK_NUM);
		}
	}

	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING, _task.getTitle());
	}

	private Task retrieveTaskFromUI() throws InvalidTaskException {
		Task task = _controller.getUI().getTask(_taskIndexes.remove(0));
		return task;
	}

	//@author A0121555M
	public void undo() {
		int[] taskIndexes = ArrayUtils.toPrimitive(_taskIndexes.toArray(new Integer[_taskIndexes.size()]));
		new UnmarkCommand(taskIndexes).execute();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndexes:" + _taskIndexes.toString());
		return sb.toString();
	}
}
