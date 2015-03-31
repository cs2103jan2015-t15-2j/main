//@author A0121555M
package taskie.database;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import taskie.exceptions.ConfigurationFailedException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NStorage implements IStorage {
	private static final String DATABASE_FILENAME = "taskie.txt";

	private Logger _logger;
	private Path _databasePath;
	private FileReaderWriter _db;
	private Gson _gson;
	private ArrayList<Task> _tasks;
	
	public NStorage(String storageDir) throws IOException {
		this(FileSystems.getDefault().getPath(storageDir));
	}

	public NStorage(Path storageDir) throws IOException {
		try {
			_databasePath = storageDir.resolve(DATABASE_FILENAME);
			_logger = Logger.getLogger(NStorage.class.getName());
			_db = new FileReaderWriter(_databasePath);
			_gson = new Gson();
			_tasks = retrieveTaskList();
		} catch (TaskRetrievalFailedException ex) {
			ex.getMessage();
		} 
		
		_logger.log(Level.INFO, "NStorage Initialized at: " + this.getStorageLocation());
		setStorageLocation(storageDir.toString());
	}

	public String getStorageLocation() {
		return _databasePath.toAbsolutePath().getParent().toString();
	}

	public void setStorageLocation(String storageDir) {
		try {
			Path newPath = FileSystems.getDefault().getPath(storageDir);
			Path newDatabasePath = _databasePath.resolve(DATABASE_FILENAME);

			if ( newPath.toString().equals(this.getStorageLocation()) ) {
				return;
			}
			
			if (_db != null) {
				_db.close();
			}

			_logger.log(Level.INFO, "Attempting to change storage location to: " + newPath.toString());
			migrateExistingDatabaseFile(_databasePath, newDatabasePath);
			_databasePath = newDatabasePath;

			_db = new FileReaderWriter(_databasePath);
			_logger.log(Level.INFO, "Successfully changed storage location to: " + _databasePath.toString());				
		} catch (ConfigurationFailedException ex) {
			ex.getMessage();
		} catch (IOException ex) {
			ex.getMessage();
		}
	}
	
	//@author A0121555M
	public void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		try {
			this.addToTaskList(task);
			this.rewriteDatabase();
		} catch (IOException e) {
			throw new TaskModificationFailedException(e);
		}
	}

	public void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		try {
			boolean status = this.removeFromTaskList(task);
			if (status) {
				this.rewriteDatabase();
			} else {
				throw new TaskModificationFailedException();
			}
		} catch (IOException e) {
			throw new TaskModificationFailedException(e);
		}
	}

	public void updateTask(Task oldTask, Task newTask) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		try {
			boolean status = this.removeFromTaskList(oldTask);
			if (status) {
				this.addToTaskList(newTask);
				this.rewriteDatabase();
			} else {
				throw new TaskModificationFailedException();
			}
		} catch (IOException e) {
			throw new TaskModificationFailedException(e);
		}
	}
	
	public void clearAllTasks() throws TaskModificationFailedException {
		try {
			_tasks.clear();
			this.rewriteDatabase();
		} catch (IOException e) {
			throw new TaskModificationFailedException(e);
		}
	}

	private void addToTaskList (Task newTask) {
		if (_tasks == null) {
			_tasks = new ArrayList<Task>();
		}

		_logger.log(Level.INFO, "Writing: " + newTask.getTitle());
		_tasks.add(newTask);
	}

	private boolean removeFromTaskList (Task taskToRemove) {
		_logger.log(Level.INFO, "Removing: " + taskToRemove.getTitle());
		boolean removed = false;
		int x = 0;

		while (!removed && x < _tasks.size()) {
			Task tempTask = _tasks.get(x);
			if (tempTask.equals(taskToRemove)) {
				removed = true;
				_tasks.remove(x);
				break;
			}
			x++;
		}
		return removed;
	}

	private void rewriteDatabase() throws IOException {
		Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
		String json = _gson.toJson(_tasks, listType);
		_db.write(json);
	}

	public void close() throws IOException {
		_logger.log(Level.INFO, "Closing Storage");
		_db.close();
	}

	//@author A0135137L
	private void migrateExistingDatabaseFile(Path oldDirectory, Path newDirectory) throws ConfigurationFailedException {
		try {
			File oldFile = FileUtils.getFile(oldDirectory.toString());
			File newDir = FileUtils.getFile(newDirectory.getParent().toString());

			if (Files.exists(newDirectory)) {
				Files.delete(newDirectory);
			}
			FileUtils.moveFileToDirectory(oldFile, newDir, true);

		} catch (IOException ex) {
			throw new ConfigurationFailedException();
		}
	}
	
	//@author A0097582N	
	public ArrayList<Task> retrieveTaskList() throws TaskRetrievalFailedException {
		try {
			
			String json = _db.read();
			if (json.equals("")) {
				return new ArrayList<Task>();
			}
			
			Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
			_tasks = _gson.fromJson(json, listType);
		} catch (IOException e) {
			throw new TaskRetrievalFailedException(e);
		}
		
		return _tasks;
	}
		
	public ArrayList<Task> getTaskList()  {
		return _tasks;
	}
}
