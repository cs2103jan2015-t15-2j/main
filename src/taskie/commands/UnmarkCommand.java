/**
 * class representing an unmark command.
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;


import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;


import edu.emory.mathcs.backport.java.util.Arrays;

import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Task;

public class UnmarkCommand extends AbstractCommand {
	private CommandType _commandType = CommandType.MARK;
<<<<<<< HEAD
	private ArrayList<Integer> _taskIndexes;
=======
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
	private Task _task;
	private int[] _taskIndexes;

	//@author A0121555M
	public UnmarkCommand(int itemNumber) {
<<<<<<< HEAD
		_taskIndexes = new ArrayList<Integer>();
		_taskIndexes.add(itemNumber);
=======
		_taskIndexes = new int[] { itemNumber };
	}
	
	public UnmarkCommand(int[] itemNumbers) {
		_taskIndexes = itemNumbers;
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
	}

	//@author A0097582N
	public UnmarkCommand(Task task) {
		_task = task;
	}

	public UnmarkCommand(int[] taskIndexes) {
		_taskIndexes = new ArrayList<Integer>();
		for(int i=0;i<taskIndexes.length;i++){
			_taskIndexes.add(taskIndexes[i]);
		}
	}

	public CommandType getCommandType() {
		return _commandType;
	}

	public Task getTask() {
		return _task;
	}

	public void execute() {
<<<<<<< HEAD
		try {
			while(_taskIndexes.size()>0){
				_task = retrieveTaskFromUI();
=======
		for ( int x = 0; x < _taskIndexes.length; x++ ) {
			try {
				int index = _taskIndexes[x];
				
				_task = retrieveTaskFromUI(index);
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
				Task updatedTask = new Task(_task);
	
				if (_task.isDone()) {
					updatedTask.setTaskUndone();
					_controller.getUI().display(DisplayType.ERROR, formatUnmarkString());
				} else {
					_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.TASK_ALREADY_NOT_DONE);
				}
				try {
					_controller.getStorage().updateTask(_task, updatedTask);
				} catch (TaskTypeNotSupportedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				} catch (TaskModificationFailedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				}

			} catch (InvalidTaskException e) {
				_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.INVALID_TASK_NUM);

			}
		}
	}

	private String formatUnmarkString() {
		return String.format(taskie.models.Messages.UNMARK_STRING, _task.getTitle());
	}


	private Task retrieveTaskFromUI() throws InvalidTaskException {
		Task task = _controller.getUI().getTask(_taskIndexes.remove(0));

		return task;
	}

	//@author A0121555M
	public void undo() {

		int[] taskIndex = ArrayUtils.toPrimitive(_taskIndexes.toArray(new Integer[_taskIndexes.size()]));
		new MarkCommand(taskIndex).execute();

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");

		sb.append("TaskIndexes:" + _taskIndexes.toString());
		return sb.toString();
	}
}
