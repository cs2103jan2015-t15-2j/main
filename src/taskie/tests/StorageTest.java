package taskie.tests;

import static org.junit.Assert.assertEquals;
import taskie.database.NStorage;
import taskie.database.IStorage;
import taskie.database.FileReaderWriter;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.database.Configuration;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.lang.reflect.Type;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StorageTest {
	
	private static final String DEFAULT_STORAGE_DIRECTORY = System.getProperty("user.home");
	private static final String DATABASE_FILENAME = "taskie.txt";
	private static final String CONFIG_FILENAME = "config.txt";
	
	private static IStorage _storage;
	private static Configuration _config;
	private static Path _databasePath;
	private static Path _configPath;
	private static ArrayList<Task> _tasks;
	private static FileReaderWriter _configReaderWriter;
	private static FileReaderWriter _dbReaderWriter;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@Before
	public void setUpBeforeTest() throws Exception {
		_storage = new NStorage();
		
		//Create config file
		String configPath = DEFAULT_STORAGE_DIRECTORY + "/" + CONFIG_FILENAME;
		_configPath = FileSystems.getDefault().getPath(configPath);
		_configReaderWriter = new FileReaderWriter(_configPath);
		
		//Create database file
		String databasePath = DEFAULT_STORAGE_DIRECTORY + "/" + DATABASE_FILENAME;
		_databasePath = FileSystems.getDefault().getPath(databasePath);
		_dbReaderWriter = new FileReaderWriter(_databasePath);
		
		//Initialize file content 
		LocalDateTime now = LocalDateTime.of(2015, 3, 29, 17, 00);
		_tasks = new ArrayList<Task>();	
		Task deadlinedTask = new Task("Deadlined Task", now.toLocalDate());
		Task timedTask = new Task("Timed Task", now.toLocalDate(), now.plusDays(1).toLocalDate());
		Task floatingTask = new Task("Floating Task");	
		_tasks.add(deadlinedTask);
		_tasks.add(timedTask);
		_tasks.add(floatingTask);
		Type listType = new TypeToken<ArrayList<Task>>(){}.getType();
		Gson gson = new Gson();
		String json = gson.toJson(_tasks, listType);
		_configReaderWriter.write(databasePath);
		_dbReaderWriter.write(json);
		
	}
	
	@After
	public void cleanUpAfterTest() throws Exception {
		Files.delete(_configPath);
		Files.delete(_databasePath);
	}
	
	@Test
	public void testGetStorageLocation() throws Exception {
		//database stored in default location
		assertEquals(_databasePath.getParent().toString(), _storage.getStorageLocation());
		
		//database stored in set location
		Path newDatabasePath = FileSystems.getDefault().getPath("D:\\"); 
		Files.move(_databasePath, newDatabasePath, StandardCopyOption.REPLACE_EXISTING);
		_configReaderWriter.write("D:\\");
		_databasePath = newDatabasePath;
		assertEquals(_databasePath.getParent().toString(), _storage.getStorageLocation());
	}
	/*
	@Test
	public void testSetStorageLocation() {
		_storage.setStorageLocation("C:\\Users\\Haihui\\Desktop");
		assertEquals(_databasePath.toString(), _storage.getStorageLocation());
		
	}
	
	@Test
	public void testAddTask() {
		
	}
	
	@Test
	public void testDeleteTask() {
		
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
	
	*/
	
	
	
	

}
