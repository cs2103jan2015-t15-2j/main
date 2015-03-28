//@author A0121555M
package taskie.database;

import java.io.IOException;
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
	private static final String NEWLINE = System.lineSeparator();

	private Controller _controller;
	private Logger _logger;
	private Path _databasePath;
	private FileReaderWriter _db;
	private Gson _gson;
	private ArrayList<String> _tasks;

	public NStorage() throws IOException {
		_gson = new Gson();
		_logger = Logger.getLogger(NStorage.class.getName());

		String filePath = DEFAULT_STORAGE_DIRECTORY + "/" + DATABASE_FILENAME;
		_databasePath = FileSystems.getDefault().getPath(filePath).toAbsolutePath();
		_db = new FileReaderWriter(_databasePath);

		_logger.log(Level.INFO, "NStorage Initialized at: " + this.getStorageLocation());
	}

	public NStorage(String storageDir) {
		super();
		this.setStorageLocation(storageDir);
	}

	public NStorage(Path storageDir) {
		super();
		this.setStorageLocation(storageDir.toString());
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

		try {
			String json = _db.read();
			_tasks = _gson.fromJson(json, new TypeToken<ArrayList<String>>() {
			}.getType());

			if (_tasks != null) {
				for (String jsonTask : _tasks) {
					Task task = _gson.fromJson(jsonTask, Task.class);
					TaskType type = task.getTaskType();

					ArrayList<Task> list = map.get(type);
					if (list == null) {
						list = new ArrayList<Task>();
						map.put(type, list);
					}
					list.add(task);
				}
			}
		} catch (IOException e) {
			throw new TaskRetrievalFailedException(e);
		}

		return map;
	}

	@Override
	public void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		Iterator<Entry<TaskType, ArrayList<Task>>> it = hm.entrySet().iterator();
		while (it.hasNext()) {
			HashMap.Entry<TaskType, ArrayList<Task>> pair = (HashMap.Entry<TaskType, ArrayList<Task>>) it.next();
			ArrayList<Task> tasks = pair.getValue();
			_tasks.clear();

			for (Task task : tasks) {
				addTask(task);
			}
		}

		try {
			this.rewriteDatabase();
		} catch (IOException e) {
			throw new TaskModificationFailedException(e);
		}
	}

	//@author A0121555M
	public void addTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		try {
			String json = _gson.toJson(task);
			this.addToDatabase(json);
			this.rewriteDatabase();
		} catch (IOException e) {
			throw new TaskModificationFailedException(e);
		}
	}

	public void deleteTask(Task task) throws TaskTypeNotSupportedException, TaskModificationFailedException {
		try {
			String json = _gson.toJson(task);
			boolean status = this.removeFromDatabase(json);
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
			String jsonOld = _gson.toJson(oldTask);
			String jsonNew = _gson.toJson(newTask);
			boolean status = this.removeFromDatabase(jsonOld);
			if (status) {
				this.addToDatabase(jsonNew);
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

	private void addToDatabase(String line) {
		if (_tasks == null) {
			_tasks = new ArrayList<String>();
		}

		_logger.log(Level.INFO, "Writing: " + line);
		_tasks.add(line);
	}

	private boolean removeFromDatabase(String line) {
		_logger.log(Level.INFO, "Removing: " + line);
		boolean removed = false;
		int x = 0;

		while (!removed && x < _tasks.size()) {
			String str = _tasks.get(x);
			if (str.equals(line)) {
				removed = true;
				_tasks.remove(x);
				break;
			}
			x++;
		}

		return removed;
	}

	private void rewriteDatabase() throws IOException {
		String json = _gson.toJson(_tasks);
		_db.write(json);
	}

	public void close() throws IOException {
		_logger.log(Level.INFO, "Closing Storage");
		_db.close();
	}

	//@author A0097582N
	public ArrayList<Task> getTaskList() throws TaskRetrievalFailedException {
		ArrayList<Task> tasks = new ArrayList<Task>();
		try {
			String json = _db.read();
			_tasks = _gson.fromJson(json, new TypeToken<ArrayList<String>>() {
			}.getType());
			for (String jsonTask : _tasks) {
				Task task = _gson.fromJson(jsonTask, Task.class);
				tasks.add(task);
			}

		} catch (IOException e) {
			throw new TaskRetrievalFailedException(e);
		}
		return tasks;

	}

	public void deleteTaskList() throws TaskRetrievalFailedException {
		_db.deleteFile(_databasePath);
	}

}
