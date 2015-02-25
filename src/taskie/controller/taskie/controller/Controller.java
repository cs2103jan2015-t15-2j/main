/**
 * Class to perform tasks needed to achieve functionality. Interface between front and back end.
 * Still under development
 * Bugs: none known
 *
 * @author       A0097582N
 */

package taskie.controller;


import java.util.Stack;

import taskie.commands.AddCommand;
import taskie.commands.ICommand;
import taskie.models.Task;
import taskie.database.IStorage;


public class Controller{
	private Stack<ICommand> _undoCommandStack; //stack to keep track of past actions for undo command	
	private IStorage _storage;
	
	public Controller(){
		_undoCommandStack = new Stack<ICommand>();
	}
	
	public void executeCommand(ICommand command){
	
		
		switch(command.getCommandType()){
			case ADD: 		executeAddCommand((AddCommand) command);
							break;
							
			case UPDATE: 	executeUpdateCommand();
							break;
		
			case VIEW:		executeViewCommand();
							break;
			
			case DELETE:	executeDeleteCommand();
							break;
			
			case UNDO:		executeUndoCommand();
							break;
			
			default:		executeDefault();
		}
		
	}


	private void executeAddCommand(AddCommand command) {
		determineTaskTypeAndAdd(command.getTaskToAdd());
		addToUndoStack(command); //to keep track of past actions for undo command
			
	}

	
	private void addToUndoStack(AddCommand command) {
		_undoCommandStack.push(command);
		
	}

	private void determineTaskTypeAndAdd(Task taskToAdd) {
		
		if(taskToAdd.getStartTime()==null 
				&& taskToAdd.getEndTime()==null){    //no time added, i.e floating task
			_storage.addFloatingTask(taskToAdd);
		}
		else if(taskToAdd.getStartTime()==null 
				^ taskToAdd.getEndTime()==null){	 //1 time added, i.e deadline task
			_storage.addDeadlineTask(taskToAdd);
		} else{
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