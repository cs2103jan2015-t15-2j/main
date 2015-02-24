package taskie.commands;
/**
 * class representing an add command.
 * Still under development
 * Bugs: none known
 *
 * @author       A0097582N
 */

import taskie.models.CommandType;
import taskie.models.Task;



public class AddCommand implements ICommand {
		private Task _task;
		private CommandType _commandType= CommandType.ADD;
		
		public AddCommand(){
			_task=null;
		}
		
		public AddCommand(Task task){
			_task=task;
		}
		
		public void setTaskName(String taskName){
			if(_task==null){
				_task=new Task(taskName);
			}
			else{
				_task.setTitle(taskName);
			}
			
		}
		
		public Task getTaskToAdd(){
			return _task;
		}
		
		public CommandType getCommandType(){
			return _commandType;
		}
}
