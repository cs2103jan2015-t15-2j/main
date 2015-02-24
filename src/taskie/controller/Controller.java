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
}