package taskie.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import taskie.Taskie;
import taskie.database.Storage;
import taskie.database.IStorage;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskTypeNotSupportedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.parser.CommandParser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.joestelmach.natty.CalendarSource;

public class StorageTest {
	
	private static IStorage storage;
	private static Task[] tasks;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		storage = new Storage();
		tasks = new Task[6];

	}
	
	@Before
	public void setUp() throws Exception {
		this.generateTasks();
	}
	
	@Test
	public void testAddTask() throws TaskTypeNotSupportedException, TaskModificationFailedException, TaskRetrievalFailedException {
		storage.addTask(tasks[0]);
		storage.addTask(tasks[1]);
		storage.addTask(tasks[2]);
		storage.addTask(tasks[3]);
		storage.addTask(tasks[4]);
		storage.addTask(tasks[5]);
		
		HashMap<TaskType, ArrayList<Task>> hm = storage.retrieveTaskMap();
		
		ArrayList<Task> deadlinedTasks = new ArrayList<Task>();
		ArrayList<Task> timedTasks = new ArrayList<Task>();
		ArrayList<Task> floatingTasks = new ArrayList<Task>();
		
		deadlinedTasks = hm.get(TaskType.DEADLINE);
		timedTasks = hm.get(TaskType.TIMED);
		floatingTasks = hm.get(TaskType.FLOATING);
		
		assertEquals(tasks[0].toString(), deadlinedTasks.get(0).toString());
		assertEquals(tasks[1].toString(), deadlinedTasks.get(1).toString());
		
		assertEquals(tasks[2].toString(), timedTasks.get(0).toString());
		assertEquals(tasks[3].toString(), timedTasks.get(1).toString());
		
		assertEquals(tasks[4].toString(), floatingTasks.get(0).toString());
		assertEquals(tasks[5].toString(), floatingTasks.get(1).toString());
		
	}
	
	@Test
	public void testDeleteTask() {
		
	}
	
	@Test
	public void testUpdateTask(){
		
	}
	
	private void generateTasks() {
		
		//Generate deadlined tasks
		LocalDate dlTask1Ld = LocalDate.of(2015, 2, 10);
		LocalDate dlTask2Ld = LocalDate.of(2015, 2, 11);
		LocalTime dlTask2Lt = LocalTime.of(15, 00);
		Task dlTask1 = new Task("deadline1", dlTask1Ld);
		Task dlTask2 = new Task("deadline2", dlTask2Ld, dlTask2Lt);
		tasks[0] = dlTask1;
		tasks[1] = dlTask2;
		
		//Generate timed tasks
		LocalDate tTask3sd = LocalDate.of(2015, 2, 12);
		LocalDate tTask3ed = LocalDate.of(2015, 2, 13);
		LocalDate tTask4sd = LocalDate.of(2015, 2, 13);
		LocalDate tTask4ed = LocalDate.of(2015, 2, 15);
		LocalTime tTask4st = LocalTime.of(16, 00);
		LocalTime tTask4et = LocalTime.of(10, 00);
		tasks[2] = new Task("timed3", tTask3sd, tTask3ed);
		tasks[3] = new Task("timed4", tTask4sd, tTask4st, tTask4ed, tTask4et);
		
		//Generate floating tasks
		tasks[4] = new Task("time5");
		tasks[5] = new Task("time6");
		
		
	}
	


}
