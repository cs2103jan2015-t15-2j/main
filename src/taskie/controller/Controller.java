package taskie.controller;

import taskie.commands.ICommand;
import taskie.models.CommandType;

public class Controller{
	
	
	
	public Controller(){

	}
	
	public void executeCommand(ICommand command){
	
		
		switch(command.getCommandType()){
			case ADD: 		executeAddCommand();
							break;
							
			case UPDATE: 	executeUpdateCommand();
							break;
		
			case VIEW:		executeViewCommand();
							break;
			
			case DELETE:	executeDeleteCommand();
							break;
			
			case UNDO:		executeUndoCommand();
							break;
			
			case SEARCH:	executeSearchCommand();
							break;
			
			default:		executeDefault();
		}
		
	}

	private void executeDefault() {
		// TODO Auto-generated method stub
		
	}

	private void executeSearchCommand() {
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