/**
 * class representing a update command. 
 * Still under development
 * Bugs: none known
 *
 */
//@author A0097582N

package taskie.commands;

<<<<<<< HEAD
import java.util.ArrayList;

import org.apache.commons.lang.ArrayUtils;

=======
import edu.emory.mathcs.backport.java.util.Arrays;
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Task;

public class MarkCommand extends AbstractCommand {

	private CommandType _commandType = CommandType.MARK;
	private Task _task;
<<<<<<< HEAD
	private ArrayList<Integer> _taskIndexes;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndexes=new ArrayList<Integer>();
		_taskIndexes.add(itemNumber);
	}
	

=======
	private int[] _taskIndexes;

	//@author A0121555M
	public MarkCommand(int itemNumber) {
		_taskIndexes = new int[] { itemNumber };
	}
	
	public MarkCommand(int[] itemNumbers) {
		_taskIndexes = itemNumbers;
	}
	
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
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
					_controller.getUI().display(DisplayType.ERROR, taskie.models.Messages.TASK_ALREADY_DONE);
				} else {
					updatedTask.setTaskDone();
<<<<<<< HEAD
				}
				try {
					_controller.getStorage().updateTask(_task, updatedTask);
					_controller.getUI().display(DisplayType.SUCCESS, formatMarkString());
=======
					_controller.getUI().display(DisplayType.SUCCESS, formatMarkString());
				}
				try {
					_controller.getStorage().updateTask(_task, updatedTask);
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
				} catch (TaskTypeNotSupportedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				} catch (TaskModificationFailedException e) {
					_controller.getUI().display(DisplayType.ERROR, e.getMessage());
				}
<<<<<<< HEAD
			}
		} catch (InvalidTaskException e) {
				_controller.getUI().display(DisplayType.ERROR,taskie.models.Messages.INVALID_TASK_NUM);
=======
			} catch (InvalidTaskException e) {
				_controller.getUI().display(DisplayType.ERROR,taskie.models.Messages.INVALID_TASK_NUM);
			}
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
		}
	}

	private String formatMarkString() {
		return String.format(taskie.models.Messages.MARK_STRING, _task.getTitle());
	}

<<<<<<< HEAD
	private Task retrieveTaskFromUI() throws InvalidTaskException {
		Task task = _controller.getUI().getTask(_taskIndexes.remove(0));
=======
	private Task retrieveTaskFromUI(int index) throws InvalidTaskException {
		Task task = _controller.getUI().getTask(index);
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
		return task;
	}

	//@author A0121555M
	public void undo() {
<<<<<<< HEAD
		int[] taskIndexes = ArrayUtils.toPrimitive(_taskIndexes.toArray(new Integer[_taskIndexes.size()]));
		new UnmarkCommand(taskIndexes).execute();
=======
		new UnmarkCommand(_taskIndexes).execute();
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CommandType:" + _commandType + ",");
<<<<<<< HEAD
		sb.append("TaskIndexes:" + _taskIndexes.toString());
=======
		sb.append("TaskIndex:" + Arrays.toString(_taskIndexes));
>>>>>>> dba536869dfafda0e0a1bc47be929489418e5501
		return sb.toString();
	}
}
