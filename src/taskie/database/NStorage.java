//@author A0121555M
package taskie.database;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskie.exceptions.FileExistsException;
import taskie.exceptions.StorageLocationInvalidException;
import taskie.exceptions.StorageMigrationFailedException;
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
			_logger = Logger.getLogger(NStorage.class.getName());
			_gson = new Gson();
			this.readDatabaseFile(storageDir);
		} catch (TaskRetrievalFailedException ex) {
			ex.getMessage();
		}

		_logger.log(Level.INFO, "NStorage Initialized at: " + this.getStorageLocation());
	}

	public Path getStorageLocation() {
		return _databasePath.toAbsolutePath().getParent();
	}
	
	public void setStorageLocation(Path newDirectory) throws StorageLocationInvalidException, FileExistsException, StorageMigrationFailedException {
		this.setStorageLocation(newDirectory, false);
	}

	public void setStorageLocation(Path newDirectory, boolean overwrite) throws StorageLocationInvalidException, FileExistsException, StorageMigrationFailedException {
		if (newDirectory.toString().equals(this.getStorageLocation())) {
			return;
		}

		if (!Files.isDirectory(newDirectory)) {
			throw new StorageLocationInvalidException(newDirectory.toString());
		}

		try {
			if (_db != null) {
				_db.close();
			}

			_logger.log(Level.INFO, "Attempting to change storage location to: " + newDirectory.toString());
			
			try {
				migrateFiles(this.getStorageLocation(), newDirectory, overwrite);
			} catch ( FileExistsException e ) {
				this.readDatabaseFile(newDirectory);
				throw new FileExistsException(e);
			}

			this.readDatabaseFile(newDirectory);
			_logger.log(Level.INFO, "Successfully changed storage location to: " + _databasePath.toString());
		} catch (IOException e) {
			throw new StorageMigrationFailedException(e);
		} catch (TaskRetrievalFailedException e) {
			throw new StorageMigrationFailedException(e);
		}
	}

	private void readDatabaseFile(Path storageDir) throws TaskRetrievalFailedException, IOException {
		_databasePath = storageDir.resolve(DATABASE_FILENAME);
		_db = new FileReaderWriter(_databasePath);
		
		if ( _tasks != null ) {
			_tasks.clear();
		}
		
		_tasks = retrieveTaskList();
	}

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

	private void addToTaskList(Task newTask) {
		if (_tasks == null) {
			_tasks = new ArrayList<Task>();
		}

		_logger.log(Level.INFO, "Writing: " + newTask.getTitle());
		_tasks.add(newTask);
	}

	private boolean removeFromTaskList(Task taskToRemove) {
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
		Type listType = new TypeToken<ArrayList<Task>>() {
		}.getType();
		String json = _gson.toJson(_tasks, listType);
		_db.write(json);
	}

	public void close() throws IOException {
		_logger.log(Level.INFO, "Closing Storage");
		_db.close();
	}

	private void migrateFiles(Path oldDirectory, Path newDirectory, boolean overwrite) throws FileExistsException, StorageMigrationFailedException {
		_logger.log(Level.FINE, "Moving from: " + oldDirectory.toString() + " to " + newDirectory.toString());
		
		try {
			Path oldDatabasePath = oldDirectory.resolve(DATABASE_FILENAME);
			Path newDatabasePath = newDirectory.resolve(DATABASE_FILENAME);
			_db.moveFile(oldDatabasePath, newDatabasePath, overwrite);
		} catch (IOException e) {
			throw new StorageMigrationFailedException(e);
		}
	}

	//@author A0135137L
	public ArrayList<Task> retrieveTaskList() throws TaskRetrievalFailedException {
		try {
			String json = _db.read();
			if (json.equals("")) {
				return new ArrayList<Task>();
			}

			Type listType = new TypeToken<ArrayList<Task>>() {
			}.getType();
			return _gson.fromJson(json, listType);
		} catch (IOException e) {
			throw new TaskRetrievalFailedException(e);
		}
	}

	public ArrayList<Task> getTaskList() {
		return _tasks;
	}
}
