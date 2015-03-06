//@author A0135137L

package taskie.database;
import taskie.models.Task;
import java.util.ArrayList;
import java.util.HashMap;


public interface IStorage {
	void setStorageLocation(String fileDir); 
	
	HashMap<String, ArrayList<Task>> retrieveTaskMap();

	void storeTaskMap(HashMap<String, ArrayList<Task>> hm);  

	void addFloatingTask(Task taskToAdd); 

	void addDeadlinedTask(Task taskToAdd);

	void addTimedTask(Task taskToAdd); 

	void deleteFloatingTask(Task taskToDelete); 
	
	void deleteDeadlinedTask(Task taskToDelete); 
	
	void deleteTimedTask(Task timeToDelete);

	void updateTask(Task oldTask, Task newTask);
}
