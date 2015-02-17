package taskie.commands;

import taskie.models.Task;



public class AddCommand implements ICommand {
		Task _task;
		
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
			
		}
		
		public Task getTaskToAdd(){
			return _task;
		}
}
