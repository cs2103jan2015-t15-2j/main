//@author A0135137L

package taskie.database;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;
import taskie.models.TaskType;

import java.util.ArrayList;
import java.util.HashMap;


public interface IStorage {
	String getStorageLocation();
	
	void setStorageLocation(String fileDir); 
	
	HashMap<TaskType, ArrayList<Task>> retrieveTaskMap();

	void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm);  



	void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void updateTask(Task oldTask, Task newTask) throws TaskTypeNotSupportedException, TaskModificationFailedException;
}
