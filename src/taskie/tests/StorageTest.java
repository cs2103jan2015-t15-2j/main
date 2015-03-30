package taskie.tests;

import static org.junit.Assert.assertEquals;
import taskie.Controller;
import taskie.database.NStorage;
import taskie.database.FileReaderWriter;
import taskie.models.Task;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class StorageTest {
	
	private static final String DEFAULT_STORAGE_DIRECTORY = System.getProperty("user.home");
	private static final String DATABASE_FILENAME = "taskie.txt";
	private static final String CONFIG_FILENAME = "config.txt";
	
	private static NStorage _storage;
	private static Path _databasePath;
	private static Path _configPath;
	private static ArrayList<Task> _tasks;

	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String configPath = DEFAULT_STORAGE_DIRECTORY + "/" + CONFIG_FILENAME;
		_configPath = FileSystems.getDefault().getPath(configPath);
		_storage = new NStorage(configPath);
		_databasePath = Controller.getInstance().getConfiguration().getDatabasePath();
		_tasks = new ArrayList<Task>();
	}
	
	@Before
	public void setUpBeforeTest() throws Exception {
	}
	
	@After
	public void ClearUpAfterTest() throws Exception {
	}

	@AfterClass
	public static void cleanUpAfterClass() throws Exception {
		Files.delete(_configPath);
		Files.delete(_databasePath);
	}

	@Test
	public void testGetStorageLocation() throws Exception {
		assertEquals(_databasePath.getParent().toString(), _storage.getStorageLocation());
	}
	
	@Test
	public void testSetStorageLocation() {
		//set database to directory that is existed
		_storage.setStorageLocation("D:\\");
		_databasePath = FileSystems.getDefault().getPath("D:\\" + DATABASE_FILENAME);
		assertEquals("D:\\", _storage.getStorageLocation());
				
		//set database to directory that is not existed
		_storage.setStorageLocation("D:\\User");
		_databasePath = FileSystems.getDefault().getPath("D:\\User\\" + DATABASE_FILENAME);
		assertEquals("D:\\User", _storage.getStorageLocation());
		
		//set database to invalid directory 
	
	}
	
	
	@Test
	public void testAddTask() throws IOException, TaskTypeNotSupportedException, TaskModificationFailedException  {
		Gson gson = new Gson();
		FileReaderWriter frw = new FileReaderWriter(_databasePath);
		Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
		LocalDateTime now = LocalDateTime.of(2015, 3, 29, 17, 00);
		String json;

		//_tasks = new ArrayList<Task>();
		Task deadlinedTask = new Task("Deadlined Task 1", now.toLocalDate());
		Task timedTask = new Task("Timed Task 1", now.toLocalDate(), now.plusDays(1).toLocalDate());
		Task floatingTask = new Task("Floating Task 1");
		
		_tasks.add(deadlinedTask);
		_tasks.add(timedTask);
		_tasks.add(floatingTask);
		
		_storage.addTask(deadlinedTask);
		_storage.addTask(timedTask);
		_storage.addTask(floatingTask);
		
		//Add deadlined task
		_tasks.add(deadlinedTask);
		json = gson.toJson(_tasks, listType);
		_storage.addTask(deadlinedTask);
		assertEquals(json, frw.read());
		
		//Add timed task
		_tasks.add(timedTask);
		json = gson.toJson(_tasks, listType);
		_storage.addTask(timedTask);
		assertEquals(json, frw.read());
		
		//Add floating task
		_tasks.add(floatingTask);
		json = gson.toJson(_tasks, listType);
		_storage.addTask(floatingTask);
		assertEquals(json, frw.read());
	}
	

	
	@Test
	public void testDeleteTask() throws IOException, TaskTypeNotSupportedException, TaskModificationFailedException {
		Gson gson = new Gson();
		FileReaderWriter frw = new FileReaderWriter(_databasePath);
		String json;
		Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
		LocalDateTime now = LocalDateTime.of(2015, 3, 29, 15, 39);
		
		Task deadlinedTask = new Task("Deadlined Task 2", now.toLocalDate());
		Task timedTask = new Task("Timed Task 2", now.toLocalDate(), now.plusDays(1).toLocalDate());
		Task floatingTask = new Task("Floating Task 2");
		
		_storage.addTask(deadlinedTask);
		_storage.addTask(timedTask);
		_storage.addTask(floatingTask);
		
		_tasks.add(deadlinedTask);
		_tasks.add(timedTask);
		_tasks.add(floatingTask);

		// Delete floating task
		_storage.deleteTask(floatingTask);
		_tasks.remove(floatingTask);
		json = gson.toJson(_tasks, listType);
		assertEquals(json, frw.read());
		
		// Delete deadlined task
		_storage.deleteTask(deadlinedTask);
		_tasks.remove(deadlinedTask);
		json = gson.toJson(_tasks, listType);
		assertEquals(json, frw.read());
		
		// Delete timed task
		_storage.deleteTask(timedTask);
		_tasks.remove(timedTask);
		json = gson.toJson(_tasks, listType);
		assertEquals(json, frw.read());
		
		// Delete unexisted task
		
	}

	@Test
	public void testUpdateTask() throws IOException, TaskTypeNotSupportedException, TaskModificationFailedException {
		
		Gson gson = new Gson();
		FileReaderWriter frw = new FileReaderWriter(_databasePath);
		Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
		LocalDateTime now = LocalDateTime.of(2015, 4, 22, 10, 39);
		
		Task oldTask = new Task("Deadlined Task 3", now.toLocalDate());
		Task newTask1 = new Task("Deadlined Task 3", now.minusDays(1).toLocalDate());
		Task newTask2 = new Task("Timed Task 3", now.toLocalDate(), now.plusDays(1).toLocalDate());
		
		_storage.addTask(oldTask);
		_tasks.add(oldTask);

		String json;
		int index;
		
		// Update task to same type of task
		_storage.updateTask(oldTask, newTask1);
		index = _tasks.indexOf(oldTask);
		_tasks.set(index, newTask1);
		json = gson.toJson(_tasks, listType);
		assertEquals(json, frw.read());

		// Update task to different type of task
		_storage.updateTask(newTask1, newTask2);
		index = _tasks.indexOf(newTask1);
		_tasks.set(index, newTask2);
		json = gson.toJson(_tasks, listType);
		assertEquals(json, frw.read());
	}


	@Test
	public void testGetTaskList() {
		assertEquals(_tasks, _storage.getTaskList());
	}
	
	@Test
	public void testDeleteDatabase() {

		
	}
	
}
