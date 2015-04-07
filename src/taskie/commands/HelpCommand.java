//@author A0135137L
package taskie.commands;

import taskie.exceptions.UndoNotSupportedException;
import taskie.models.CommandType;
import taskie.models.DisplayType;
import taskie.models.Messages;

public class HelpCommand extends AbstractCommand {
	private CommandType _commandType;
	
	public HelpCommand () {
		_commandType = null;
	}
	
	public HelpCommand (CommandType commandType) {
		_commandType = commandType;
	}

	@Override
	public void execute() {
		switch (_commandType) {
			case ADD: 
				helpAdd();
				break;
			case UPDATE:
				helpUpdate();
				break;
			case VIEW:
				helpView();
				break;
			case DELETE:
				helpDelete();
				break;
			case UNDO:
				helpUndo();
				break;
			case REDO:
				helpRedo();
				break;
			case MARK:
				helpMark();
				break;	
			case UNMARK:
				helpUnmark();
				break;	
			case DIRECTORY:
				helpDirectory();
				break;
			case HELP:
				helpAll();
				break;
			case EXIT:
				helpExit();
				break;	
		}
	}
	
	private void helpAdd() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.ADD_HELP);
	}
	
	private void helpUpdate() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.UPDATE_HELP);
	}
	
	private void helpView() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.VIEW_HELP);
	}
	
	private void helpDelete() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.DELETE_HELP);
	}
	
	private void helpUndo() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.UNDO_HELP);
	}
	
	private void helpRedo() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.REDO_HELP);
	}
	
	private void helpMark() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.MARK_HELP);
	}
	
	private void helpUnmark() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.UNMARK_HELP);
	}

	private void helpDirectory() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.DIRECTORY_HELP);
	}
	
	private void helpExit() {
		_controller.getUI().display(DisplayType.DEFAULT, Messages.EXIT_HELP);
	}
	
	
	private void helpAll() {
		_controller.getUI().display(DisplayType.DEFAULT, "Taskie Help\n");
		helpAdd();
		helpUpdate();
		helpView();
		helpDelete();
		helpUndo();
		helpRedo();
		helpMark();
		helpUnmark();
		helpDirectory();
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.HELP;
	}
	
	@Override
	public void undo() throws UndoNotSupportedException {
		throw new UndoNotSupportedException();
	}
}
