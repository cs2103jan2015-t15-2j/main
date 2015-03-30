//@author A0135137L

package taskie.database;

import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;


import java.io.IOException;
import java.util.ArrayList;


public interface IStorage {
	String getStorageLocation();

	void setStorageLocation(String fileDir);

	void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void updateTask(Task oldTask, Task newTask) throws TaskTypeNotSupportedException, TaskModificationFailedException;
	
	void clearAllTasks() throws TaskModificationFailedException;
	
	void close() throws IOException;
	
	ArrayList<Task> getTaskList() throws TaskRetrievalFailedException;
}
