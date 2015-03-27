//@author A0121555M
package taskie.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.Taskie;
import taskie.commands.AddCommand;
import taskie.commands.ICommand;
import taskie.commands.UpdateCommand;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.models.Task;
import taskie.models.ViewType;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

import com.joestelmach.natty.CalendarSource;

public class CommandTest {
	private static final LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static final LocalDateTime MAX_DATETIME = LocalDateTime.MAX;

	private static Parser _parser;

	private static LocalDate _nowDate;
	private static LocalTime _nowTime;
	private static LocalDate _laterDate;
	private static LocalTime _laterTime;
	private static LocalDateTime _now;
	private static LocalDateTime _later;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_parser = new CommandParser();
		_now = LocalDateTime.now();
		_later=LocalDateTime.now();
		
		_nowDate = _now.toLocalDate();
		_nowTime = _now.toLocalTime();
		_laterDate = _later.toLocalDate();
		_laterTime = _later.toLocalTime();

		Instant instant = _now.atZone(ZoneId.systemDefault()).toInstant();
		CalendarSource.setBaseDate(Date.from(instant));
	}
	@AfterClass
	public static void cleanUp() throws IOException, TaskRetrievalFailedException{
		Taskie.Controller = new taskie.controller.Controller();
		Taskie.Controller.getStorage().deleteTaskList();
	}
	
	@Before
	public void setUp() throws TaskRetrievalFailedException, IOException {
		Taskie.Controller = new taskie.controller.Controller();
		Taskie.Controller.getStorage().deleteTaskList();
	}

//	@Test
	public void testAddCommandFloating() throws InvalidCommandException, InvalidTaskException, TaskRetrievalFailedException, IOException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		Taskie.Controller.executeCommand(cmd);
		ArrayList<Task> list =Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo");
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}
	
	//@Test
	public void testAddCommandDeadline() throws TaskRetrievalFailedException, IOException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("bar");
		cmd.setEndDateTime(_now);
		Taskie.Controller.executeCommand(cmd);
		ArrayList<Task> list =Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("bar",_nowDate,_nowTime);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}
	
//	@Test
	public void testAddCommandTimed() throws TaskRetrievalFailedException, IOException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foobar");
		cmd.setEndDateTime(_later);
		cmd.setStartDateTime(_now);
		Taskie.Controller.executeCommand(cmd);
		ArrayList<Task> list =Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foobar",_nowDate,_nowTime,_laterDate,_laterTime);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}
	
	//@Test
	public void testViewCommandAll() throws TaskRetrievalFailedException, IOException{
		setUp();
		generateTasks();
		ViewCommand cmd = new ViewCommand(ViewType.ALL);
		cmd.execute();
	}
	@Test
	public void testUpdateCommandTaskName() throws TaskRetrievalFailedException, IOException, InvalidTaskException{
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.execute();
		ArrayList<Task> tasks=Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI().display(tasks.toArray(new Task[tasks.size()]));
		System.out.println(Taskie.Controller.getUI().getCurrentTaskList()[0]);
		UpdateCommand update = new UpdateCommand();
		update.setTaskTitleToUpdate("bar");
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		System.out.println("here: "+list.size());
		System.out.println(list.get(0));
		Task expectedTask=new Task("bar");
		assertEquals(expectedTask.toString(),list.get(0).toString());
		
	}
	

	
	private void generateTasks() {
		for(int i=0;i<10;i++){
			AddCommand cmd = new AddCommand();
			cmd.setTaskName("foo "+i);
			if(i%2==0){
				cmd.setEndDateTime(_later);
				if(i%3==0){
					cmd.setStartDateTime(_now);
				}
			}
		cmd.execute();
		}
	}

}
