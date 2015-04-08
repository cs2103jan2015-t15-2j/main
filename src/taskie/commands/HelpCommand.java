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
		_controller.getUI().display(DisplayType.SUCCESS, Messages.ADD_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.ADD_HELP_BODY);
	}
	
	private void helpUpdate() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.UPDATE_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.UPDATE_HELP_BODY);
	}
	
	private void helpView() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.VIEW_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.VIEW_HELP_BODY);
	}
	
	private void helpDelete() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.DELETE_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.DELETE_HELP_BODY);
	}
	
	private void helpUndo() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.UNDO_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.UNDO_HELP_BODY);
	}
	
	private void helpRedo() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.REDO_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.REDO_HELP_BODY);
	}
	
	private void helpMark() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.MARK_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.MARK_HELP_BODY);
	}
	
	private void helpUnmark() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.UNMARK_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.UNMARK_HELP_BODY);
	}

	private void helpDirectory() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.DIRECTORY_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.DIRECTORY_HELP_BODY);
	}
	
	private void helpExit() {
		_controller.getUI().display(DisplayType.SUCCESS, Messages.EXIT_HELP_HEADER);
		_controller.getUI().display(DisplayType.DEFAULT, Messages.EXIT_HELP_BODY);
	}
	
	
	private void helpAll() {
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
