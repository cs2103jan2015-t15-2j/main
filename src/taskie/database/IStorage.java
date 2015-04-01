//@author A0135137L
package taskie.database;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import taskie.exceptions.FileExistsException;
import taskie.exceptions.StorageLocationInvalidException;
import taskie.exceptions.StorageMigrationFailedException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;

public interface IStorage {
	Path getStorageLocation();

	void setStorageLocation(Path newDirectory) throws StorageLocationInvalidException, FileExistsException, StorageMigrationFailedException;

	void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException;

	void updateTask(Task oldTask, Task newTask) throws TaskTypeNotSupportedException, TaskModificationFailedException;
	
	void clearAllTasks() throws TaskModificationFailedException;
	
	void close() throws IOException;
	
	ArrayList<Task> getTaskList() throws TaskRetrievalFailedException;
}
