//@author A0097582N
package taskie.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.Controller;
import taskie.commands.*;
import taskie.exceptions.*;
import taskie.models.Task;
import taskie.models.ViewType;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

import com.joestelmach.natty.CalendarSource;

public class CommandTest {
	private static final LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static final LocalDateTime MAX_DATETIME = LocalDateTime.MAX;

	private static Controller _controller;



	private static LocalDate _time1Date;
	private static LocalTime _time1Time;

	private static LocalDate _time2Date;
	private static LocalTime _time2Time;
	
	private static LocalDate _time3Date;
	private static LocalTime _time3Time;
	
	private static LocalDate _time4Date;
	private static LocalTime _time4Time;

	private static LocalDateTime _time1;
	private static LocalDateTime _time2;
	private static LocalDateTime _time3;
	private static LocalDateTime _time4;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_controller = Controller.getInstance();
		_time1 = LocalDateTime.of(2100,1,2,13,0);
		_time2 = _time1.plusHours(2);
		_time3 = _time1.plusDays(1);
		_time4= _time3.plusHours(2);

		
		_time1Date = _time1.toLocalDate();
		_time1Time = _time1.toLocalTime();
		
		_time2Date = _time2.toLocalDate();
		_time2Time = _time2.toLocalTime();
		
		_time3Date = _time3.toLocalDate();
		_time3Time = _time3.toLocalTime();
		
		_time4Date = _time4.toLocalDate();
		_time4Time = _time4.toLocalTime();

	}

	@AfterClass
	public static void cleanUp() throws IOException, TaskRetrievalFailedException, TaskModificationFailedException {
		_controller.getStorage().clearAllTasks();
	}

	@Before
	public void setUp() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		_controller.getStorage().clearAllTasks();
	}

	@Test
	public void testAddCommandFloating() throws InvalidCommandException,
			InvalidTaskException, TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		setUp();
		addTask("foo",null,null);
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		
		Task expectedTask = new Task("foo");
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	@Test
	public void testAddCommandDeadline() throws TaskRetrievalFailedException,
			IOException, TaskModificationFailedException {
		setUp();
		addTask("bar",null,_time1);
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		
		Task expectedTask = new Task("bar", _time1Date, _time1Time);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	@Test
	public void testAddCommandTimed() throws TaskRetrievalFailedException,
			IOException, TaskModificationFailedException {
		setUp();
		AddCommand cmd = new AddCommand();
		addTask("foobar",_time1,_time2);
		_controller.executeCommand(cmd);
		ArrayList<Task> list = _controller.getStorage().getTaskList();
	
		Task expectedTask = new Task("foobar", _time1Date, _time1Time, _time2Date,
				_time2Time);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	@Test
	public void testViewCommandAll() throws TaskRetrievalFailedException,
			IOException, TaskModificationFailedException, InvalidTaskException {
		setUp();
		generateTasks();
		ViewCommand cmd = new ViewCommand(ViewType.ALL);
		cmd.execute();
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		assertEquals(10, _controller.getUI().getCurrentTaskList().length);
	}


	@Test
	public void testUpdateCommandTaskName()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		AddCommand cmd = new AddCommand();
		addTask("foo",_time1,_time2);
		ArrayList<Task> tasks = _controller.getStorage().getTaskList();
		_controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setTaskTitleToUpdate("bar");
		update.setTaskIndex(1);
		update.execute();
		
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		Task expectedTask = new Task("bar",_time1,_time2);
		assertEquals(expectedTask.toString(), list.get(0).toString());

	}

	@Test
	public void testUpdateCommandEndTime() throws TaskRetrievalFailedException,
			IOException, InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		addTask("foo",null,_time1);
		ArrayList<Task> tasks = _controller.getStorage().getTaskList();
		_controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setEndDateToUpdate(_time2Date);
		update.setEndTimeToUpdate(_time2Time);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo",_time2);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	@Test
	public void testUpdateCommandStartEndDateTime()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		addTask("foo",_time1,_time2);
		ArrayList<Task> tasks = _controller.getStorage().getTaskList();
		_controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setEndDateToUpdate(_time3Date);
		update.setEndTimeToUpdate(_time3Time);
		update.setStartDateToUpdate(_time2Date);
		update.setStartTimeToUpdate(_time2Time);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo",_time2,_time3);

		assertEquals(expectedTask.toString(), list.get(0).toString());
	}


	
	//startdate and enddate of task is 2 hours apart, updatecommand updates startdate to the nextday
	@Test
	public void testUpdateCommandStartEndDateTimeComplex()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		addTask("foo",_time1,_time3);
		
		ArrayList<Task> tasks = _controller.getStorage().getTaskList();
		_controller.getUI().display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setStartDateToUpdate(_time2Date);
		update.setStartTimeToUpdate(_time2Time);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		
		
		Task expectedTask = new Task("foo",_time2,_time3);
		assertEquals(expectedTask.toString(), list.get(0).toString());	
	}
	
	
	
	@Test
	public void testDeleteCommand() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		addTask("foo",null,null);
		addTask("bar",null,null);
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[2]));
		DeleteCommand delete = new DeleteCommand(1);
		delete.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		assertEquals(1,list.size());
	}
	
	
	@Test 
	public void testMarkAndUnmarkCommand() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		addTask("foo",null,null);
		
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		MarkCommand mark = new MarkCommand(1);
		mark.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		assertEquals(true,list.get(0).isDone());
		
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		UnmarkCommand unmark = new UnmarkCommand(1);
		unmark.execute();
		list = _controller.getStorage().getTaskList();
		assertEquals(false,list.get(0).isDone());	
	}

	@Test
	public void testUndoAndRedoCommand() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		AddCommand cmd = new AddCommand("foo",null,null);
		_controller.executeCommand(cmd);
		UndoCommand undo = new UndoCommand();
		undo.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		assertEquals(0,list.size());
		RedoCommand redo = new RedoCommand();
		redo.execute();
		list = _controller.getStorage().getTaskList();
		assertEquals(1,list.size());
	}
	
//  @Test
	public void testDirectoryCommand(){
		String path = _controller.getStorage().getStorageLocation().toString();
		path=path.concat("/test");
		DirectoryCommand cmd = new DirectoryCommand(path,false);
		cmd.execute();
		assertEquals(path,_controller.getStorage().getStorageLocation().toString());
	}
	
	
	
	
	
	private void addTask(String taskName,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		AddCommand cmd = new AddCommand();
		cmd.setTaskName(taskName);
		System.out.println(cmd.toString());
		if(startDateTime!=null){
			cmd.setStartDateTime(startDateTime);
		}
		if(endDateTime!=null){
			cmd.setEndDateTime(endDateTime);
		}
		cmd.execute();
	}
	

	
	

	private void generateTasks() {
		for (int i = 0; i < 10; i++) {
			AddCommand cmd = new AddCommand();
			cmd.setTaskName("foo " + i);
			if (i % 2 == 0) {
				cmd.setEndDateTime(_time2);
				if (i % 3 == 0) {
					cmd.setStartDateTime(_time1);
				}
			}
			cmd.execute();
		}
	}

}
