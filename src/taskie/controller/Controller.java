
package taskie.controller;


/*
 * Class to perform tasks needed to achieve functionality. Interface between front and back end.
 * Still under development
 * Bugs: none known
 */

// @author       A0097582N




import java.util.Stack;

package taskie.controller;
import taskie.commands.AddCommand;
import taskie.commands.ICommand;
import taskie.database.IStorage;
import taskie.database.Storage;
import taskie.models.Task;
import taskie.ui.UI;

public class Controller {
	private IStorage _storage;
	private Stack<ICommand> _undoStack;
	private UI _uI;

	public Controller(UI uI) {
		_storage=new Storage();
		_undoStack = new Stack<ICommand>();
		_uI = uI;
		
		
	}

	public void executeCommand(ICommand command) {

		switch (command.getCommandType()) {
		case ADD:
			executeAddCommand((AddCommand) command);
			break;

		case UPDATE:
			executeUpdateCommand();
			break;

		case VIEW:
			executeViewCommand();
			break;

		case DELETE:
			executeDeleteCommand();
			break;

		case UNDO:
			executeUndoCommand();
			break;

		default:
			executeDefault();
		}

	}

	private void executeAddCommand(AddCommand command) {
		determineTaskTypeAndAdd(command.getTaskToAdd());
		addToUndoStack(command);
		String msgToUser=formatAddMsg(command);
		_uI.display(msgToUser);
		
	}

	private String formatAddMsg(AddCommand command) {
		Task task= command.getTaskToAdd();
		
		return "stub message";
	}

	private void addToUndoStack(AddCommand command) {
		_undoStack.push(command);
	}

	private void determineTaskTypeAndAdd(Task taskToAdd) {
			//no time added.
		if (taskToAdd.getStartTime() == null && taskToAdd.getEndTime() == null) { 
			_storage.addFloatingTask(taskToAdd);
		} else if (taskToAdd.getStartTime() == null
					^ taskToAdd.getEndTime() == null) { // 1 time added, i.e deadline task
			_storage.addDeadlineTask(taskToAdd);
		} else {
			_storage.addTimedTask(taskToAdd);
		}
	}

	private void executeDefault() {
		// TODO Auto-generated method stub

	}

	private void executeUndoCommand() {
		// TODO Auto-generated method stub

	}

	private void executeDeleteCommand() {
		// TODO Auto-generated method stub

	}

	private void executeViewCommand() {
		// TODO Auto-generated method stub

	}

	private void executeUpdateCommand() {
		// TODO Auto-generated method stub

	}

}