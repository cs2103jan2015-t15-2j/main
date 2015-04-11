/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N
package taskie.commands;

import java.util.Arrays;

import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Task;

public class MarkCommand extends AbstractCommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;

	private int[] _taskIndexes;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndexes = new int[] { itemNumber };
	}

	public MarkCommand(int[] itemNumbers) {
		_taskIndexes = itemNumbers;
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

	public void execute() {
		for (int x = 0; x < _taskIndexes.length; x++) {
			try {
				int index = _taskIndexes[x];
				_task = retrieveTaskToMark(index);
				if(_task==null){
					throw new InvalidCommandException();
				}
				Task updatedTask = new Task(_task);


				if (_task.isDone()) {
					_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.TASK_ALREADY_DONE);
				} else {
					updatedTask.setTaskDone();
					_controller.getUI().display(DisplayType.SUCCESS, formatMarkString());
				}
				try {
					_controller.setLastTask(updatedTask);
					_controller.getStorage().updateTask(_task, updatedTask);
				} catch (TaskTypeNotSupportedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				} catch (TaskModificationFailedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				}

			} catch (InvalidTaskException e) {
				_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.INVALID_TASK_NUM);
			} catch (TaskRetrievalFailedException e) {
				_controller.getUI().display(DisplayType.ERROR, e.getMessage());
			} catch (InvalidCommandException e) {
				_controller.getUI().display(DisplayType.ERROR,e.getMessage());
			}
		}
		if(_taskIndexes.length>1){
			_controller.setLastTask(null);
		}
	}

	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING, _task.getTitle());
	}

	private Task retrieveTaskToMark(int index) throws InvalidTaskException, TaskRetrievalFailedException {
		Task task = null;
		if(index==0){
			task = _controller.getLastTask();
		}else{
			task = _controller.getUI().getTask(index);
		}
		return task;
	}

	//@author A0121555M
	public void undo() {
		new UnmarkCommand(_taskIndexes).execute();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
		sb.append("TaskIndex:" + Arrays.toString(_taskIndexes));
		return sb.toString();
	}
}
