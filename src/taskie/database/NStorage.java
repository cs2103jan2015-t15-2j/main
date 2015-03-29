//@author A0121555M
package taskie.database;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.Controller;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.models.Task;
import taskie.models.TaskType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NStorage implements IStorage {
	
	private static final String DEFAULT_STORAGE_DIRECTORY = System.getProperty("user.home");
	private static final String DATABASE_FILENAME = "taskie.txt";

	private Controller _controller;
	private Logger _logger;
	private Path _databasePath;
	private FileReaderWriter _db;
	private Gson _gson;
	private ArrayList<Task> _tasks;

	public NStorage() throws IOException {
		
	//	_controller = Controller.getInstance();
		_logger = Logger.getLogger(NStorage.class.getName());
		String filePath = DEFAULT_STORAGE_DIRECTORY + "/" + DATABASE_FILENAME;
		_databasePath = FileSystems.getDefault().getPath(filePath);
		_db = new FileReaderWriter(_databasePath);
		_gson = new Gson();
		try {
			_tasks = retrieveTaskList();
		} catch (TaskRetrievalFailedException ex) {
			ex.getMessage();
		}
		
		_logger.log(Level.INFO, "NStorage Initialized at: " + this.getStorageLocation());
	}

	public NStorage(String storageDir) throws IOException, TaskRetrievalFailedException {
		this();
		setStorageLocation(storageDir);
	}

	public NStorage(Path storageDir) throws IOException, TaskRetrievalFailedException {
		this();
		setStorageLocation(storageDir.toString());
	}

	public String getStorageLocation() {
		return _databasePath.toAbsolutePath().getParent().toString();
	}

	public void setStorageLocation(String storageDir) {
		try {
			if (_db != null) {
				_db.close();
			}

			String filePath = storageDir + "/" + DATABASE_FILENAME;
			Path newDatabasePath = FileSystems.getDefault().getPath(filePath).toAbsolutePath();
			_logger.log(Level.INFO, "Attempting to change storage location to: " + _databasePath.toString());
			
			migrateExistingDatabaseFile(_databasePath, newDatabasePath);
			_logger.log(Level.INFO, "Successfully changed storage location to: " + _databasePath.toString());
			
			_databasePath = newDatabasePath;
			_db = new FileReaderWriter(_databasePath);
			_controller.getUI().display("Database is now saved at: " + _databasePath.toString());
			
		} catch (IOException ex) {
			_controller.getUI().display("Failed to set new location: " + ex.getMessage());
		}
	}

	@Override
	public HashMap<TaskType, ArrayList<Task>> retrieveTaskMap() throws TaskRetrievalFailedException {
		HashMap<TaskType, ArrayList<Task>> map = new HashMap<TaskType, ArrayList<Task>>();
		
		for (Task task : _tasks) {
			TaskType type = task.getTaskType();

			ArrayList<Task> list = map.get(type);
			if (list == null) {
				list = new ArrayList<Task>();
				map.put(type, list);
			}
			list.add(task);
		}
		
		return map;
	}

	@Override
	public void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		
		Iterator<Entry<TaskType, ArrayList<Task>>> it = hm.entrySet().iterator();
		
		while (it.hasNext()) {
			HashMap.Entry<TaskType, ArrayList<Task>> pair = (HashMap.Entry<TaskType, ArrayList<Task>>) it.next();
			ArrayList<Task> tasks = pair.getValue();

			for (Task task : tasks) {
				addTask(task);
			}
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

	private void migrateExistingDatabaseFile(Path oldDirectory, Path newDirectory) throws IOException {
		Files.move(oldDirectory, newDirectory, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
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

	public void deleteDatabase() throws TaskRetrievalFailedException {
		_db.deleteFile(_databasePath);
	}

}
