/**
 * Class to perform tasks needed to achieve functionality. Interface between front and back end.
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N
 

package taskie.controller;

import taskie.commands.AddCommand;
import taskie.commands.ICommand;
import taskie.database.IStorage;
import taskie.models.Task;

public class Controller {
	private IStorage _storage;

	public Controller() {
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
	}

	private void determineTaskTypeAndAdd(Task taskToAdd) {

		if (taskToAdd.getStartTime() == null && taskToAdd.getEndTime() == null) { // no
																					// time
																					// added,
																					// i.e
																					// floating
																					// task
			_storage.addFloatingTask(taskToAdd);
		} else if (taskToAdd.getStartTime() == null
				^ taskToAdd.getEndTime() == null) { // 1 time added, i.e
													// deadline task
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

	private void executeAddCommand() {
		// TODO Auto-generated method stub

	}
}