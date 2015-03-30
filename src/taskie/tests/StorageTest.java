package taskie.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import taskie.database.NStorage;
import taskie.database.IStorage;
import taskie.database.FileReaderWriter;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.database.Configuration;
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
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;

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
		_storage = new NStorage();
		String configPath = DEFAULT_STORAGE_DIRECTORY + "/" + CONFIG_FILENAME;
		_configPath = FileSystems.getDefault().getPath(configPath);
		_databasePath = _storage.getConfigration().getDatabasePath();
	}
	
	@Before
	public void setUpBeforeTest() throws Exception {
		LocalDateTime now = LocalDateTime.of(2015, 3, 29, 17, 00);
		_tasks = new ArrayList<Task>();
		Task deadlinedTask = new Task("Deadlined Task 0", now.toLocalDate());
		Task timedTask = new Task("Timed Task 0", now.toLocalDate(), now.plusDays(1).toLocalDate());
		Task floatingTask = new Task("Floating Task 0");
		
		_tasks.add(deadlinedTask);
		_tasks.add(timedTask);
		_tasks.add(floatingTask);
		
		_storage.addTask(deadlinedTask);
		_storage.addTask(timedTask);
		_storage.addTask(floatingTask);

	}
	
	@After
	public void ClearUpAfterTest() throws Exception {
		FileReaderWriter frw = new FileReaderWriter(_databasePath);
		_tasks.clear();
		frw.write("");
		frw.close();
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
		
		//Add deadlined task
		Task deadlinedTask = new Task("Deadlined Task 1", now.toLocalDate());
		_tasks.add(deadlinedTask);
		json = gson.toJson(_tasks, listType);
		_storage.addTask(deadlinedTask);
		assertEquals(json, frw.read());
		
		//Add timed task
		Task timedTask = new Task("Timed Task 1", now.toLocalDate(), now.plusDays(1).toLocalDate());
		_tasks.add(timedTask);
		json = gson.toJson(_tasks, listType);
		_storage.addTask(timedTask);
		assertEquals(json, frw.read());
		
		//Add floating task
		Task floatingTask = new Task("Floating Task 1");	
		_tasks.add(floatingTask);
		json = gson.toJson(_tasks, listType);
		_storage.addTask(floatingTask);
		assertEquals(json, frw.read());
	}
	

	
	@Test
	public void testDeleteTask() throws IOException, TaskTypeNotSupportedException, TaskModificationFailedException {
		Gson gson = new Gson();
		FileReaderWriter frw = new FileReaderWriter(_databasePath);
		
		System.out.println(frw.read());
		Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
		LocalDateTime now = LocalDateTime.of(2015, 3, 29, 17, 00);
		
		Task deadlinedTask = new Task("Deadlined Task 0", now.toLocalDate());
		Task timedTask = new Task("Timed Task 0", now.toLocalDate(), now.plusDays(1).toLocalDate());
		Task floatingTask = new Task("Floating Task 0");

		//Delete floating task
		_storage.deleteTask(floatingTask);
		_tasks.remove(floatingTask);
		String json = gson.toJson(_tasks, listType);
		assertEquals(json, frw.read());
		frw.close();
		
	}

	@Test
	public void testUpdateTask() {

	}

	@Test
	public void testClose() {

	}

	@Test
	public void testGetTaskList() {
		
	}
	
	@Test
	public void testDeleteDatabase() {
		
	}
	
}
