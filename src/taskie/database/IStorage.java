package taskie.database;

import taskie.models.Task;
import java.util.ArrayList;

//responsible for storing tasklists in non-volatile form
public interface IStorage {
	String storageLocation=null;
	

	Boolean setStorageLocation(String fileDir);
	
	//Retrieve all the three task lists from the default location 
	ArrayList<Task>[] retrieveTaskList();
	
	//Retrieve all the three task lists from the location fileDir
	ArrayList<Task>[] retrieveTaskList(String fileDir);
	
	//Store the three lists into the right file in the location fileDir
	Boolean storeTaskList(ArrayList<Task>[] tasklists, String fileDir);
	
	//Store the three lists into the right file in the default location
	Boolean storeTaskList(ArrayList<Task>[] tasklists);

	//Add floating task to the default location
	Boolean addFloatingTask(Task taskToAdd);
	
	//Add floating task to the location fileDir
	Boolean addFloatingTask(Task taskToAdd,String fileDir);

	//Add deadline task to the default location
	Boolean addDeadlineTask(Task taskToAdd);
	
	//Add deadline task to the the location fileDir
	Boolean addDeadlineTask(Task taskToAdd,String fileDir);

	//Add time task to the default location
	Boolean addTimedTask(Task taskToAdd);
	
	//Add time task to the the location fileDir
	Boolean addTimedTask(Task taskToAdd,String fileDir);

	void storeUpdatedTask(Task task);
}
