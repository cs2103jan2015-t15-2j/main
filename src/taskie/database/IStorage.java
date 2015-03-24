//@author A0135137L

package taskie.database;
import taskie.models.Task;
import taskie.models.TaskType;

import java.util.ArrayList;
import java.util.HashMap;


public interface IStorage {
	String getStorageLocation();
	
	void setStorageLocation(String fileDir); 
	
	HashMap<TaskType, ArrayList<Task>> retrieveTaskMap();

	void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm);  

	void addFloatingTask(Task taskToAdd); 

	void addDeadlinedTask(Task taskToAdd);

	void addTimedTask(Task taskToAdd); 

	void deleteFloatingTask(Task taskToDelete); 
	
	void deleteDeadlinedTask(Task taskToDelete); 
	
	void deleteTimedTask(Task timeToDelete);

	void updateTask(Task oldTask, Task newTask);
}
