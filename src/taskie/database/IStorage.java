//@author A0135137L

package taskie.database;

import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;
import taskie.models.TaskType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IStorage {
	String getStorageLocation();

	void setStorageLocation(String fileDir);

	HashMap<TaskType, ArrayList<Task>> retrieveTaskMap() throws TaskRetrievalFailedException;

	void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void updateTask(Task oldTask, Task newTask) throws TaskTypeNotSupportedException, TaskModificationFailedException;
	
	void close() throws IOException;
}
