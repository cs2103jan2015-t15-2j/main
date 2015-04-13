//@author A0097582N
package taskie.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.Controller;
import taskie.commands.AddCommand;
import taskie.commands.DeleteCommand;
import taskie.commands.DirectoryCommand;
import taskie.commands.MarkCommand;
import taskie.commands.RedoCommand;
import taskie.commands.UndoCommand;
import taskie.commands.UnmarkCommand;
import taskie.commands.UpdateCommand;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.InvalidTaskException;
import taskie.exceptions.TaskDateInvalidException;
import taskie.exceptions.TaskDateNotSetException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.models.Task;
import taskie.models.ViewType;

public class CommandTest {
	private static Controller _controller;

	private static LocalDate _time1Date;
	private static LocalTime _time1Time;

	private static LocalDate _time2Date;
	private static LocalTime _time2Time;
	
	private static LocalDate _time3Date;
	private static LocalTime _time3Time;
	
	private static LocalDateTime _time1;
	private static LocalDateTime _time2;
	private static LocalDateTime _time3;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_controller = Controller.getInstance();
		_time1 = LocalDateTime.of(2100,1,2,13,0);
		_time2 = _time1.plusHours(2);
		_time3 = _time1.plusDays(1);
		
		_time1Date = _time1.toLocalDate();
		_time1Time = _time1.toLocalTime();
		
		_time2Date = _time2.toLocalDate();
		_time2Time = _time2.toLocalTime();
		
		_time3Date = _time3.toLocalDate();
		_time3Time = _time3.toLocalTime();
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
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[10]));
		assertEquals(10, _controller.getUI().getCurrentTaskList().length);
	}
	
	@Test
	public void testViewCommandSearchTaskName() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException, InvalidTaskException{
		setUp();
		generateTasks();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("bar");
		cmd.execute();
		ViewCommand view = new ViewCommand(ViewType.SEARCH);
		view.setSearchKeywords("bar");
		view.execute();
		assertEquals(1,_controller.getUI().getCurrentTaskList().length);
	}
	
	
	@Test
	public void testViewCommandSearchfromStartDate() throws InvalidTaskException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		generateTasks();
		ViewCommand view = new ViewCommand(ViewType.SEARCH);
		view.setStartDate(_time3Date);
		view.execute();
		assertEquals(8,_controller.getUI().getCurrentTaskList().length);
	}
	
	@Test
	public void testViewCommandSearchByEndDate() throws InvalidTaskException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		generateTasks();
		ViewCommand view = new ViewCommand(ViewType.SEARCH);
		view.setEndDate(_time2Date);
		view.execute();
		assertEquals(7,_controller.getUI().getCurrentTaskList().length);
	}
	
	@Test
	public void testViewCommandSearchBetweenDates() throws InvalidTaskException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		generateTasks();
		ViewCommand view = new ViewCommand(ViewType.SEARCH);
		view.setStartDate(_time1Date);
		view.setEndDate(_time3Date);
		view.execute();
		assertEquals(10,_controller.getUI().getCurrentTaskList().length);
	}
	
	
	@Test
	public void testViewUpcoming() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException, InvalidTaskException{
		setUp();
		generateTasks();
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[10]));
		int[] intArr= {1,3,5};
		MarkCommand mark = new MarkCommand(intArr);
		mark.execute();
		ViewCommand view = new ViewCommand(ViewType.UPCOMING);
		view.execute();
		assertEquals(7,_controller.getUI().getCurrentTaskList().length);
	}
	
	
	@Test
	public void testUpdateCommandTaskName()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		addTask("foo",_time1,_time2);
		ArrayList<Task> tasks = _controller.getStorage().getTaskList();
		_controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand(1);
		update.setTaskTitleToUpdate("bar");
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
		UpdateCommand update = new UpdateCommand(1);
		update.setEndDateToUpdate(_time2Date);
		update.setEndTimeToUpdate(_time2Time);
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
		UpdateCommand update = new UpdateCommand(1);
		update.setEndDateToUpdate(_time3Date);
		update.setEndTimeToUpdate(_time3Time);
		update.setStartDateToUpdate(_time2Date);
		update.setStartTimeToUpdate(_time2Time);
		update.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo",_time2,_time3);

		assertEquals(expectedTask.toString(), list.get(0).toString());
	}


	
	//startdate and enddate of task is 2 hours apart, updatecommand updates startdate to the nextday
	@Test
	public void testUpdateCommandUpdateToStartTimeAfterEndTime()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		addTask("foo",_time1,_time3);
		
		ArrayList<Task> tasks = _controller.getStorage().getTaskList();
		_controller.getUI().display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand(1);
		update.setStartDateToUpdate(_time2Date);
		update.setStartTimeToUpdate(_time2Time);
		update.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		
		long difference = ChronoUnit.NANOS.between(_time1, _time2);
		
		Task expectedTask = new Task("foo",_time2,_time3.plusNanos(difference));
		assertEquals(expectedTask.toString(), list.get(0).toString());	
	}
	
	
	@Test
	public void testUpdateCommandNoIndexReference()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException, TaskDateNotSetException, TaskDateInvalidException, TaskModificationFailedException {
		setUp();
		addTask("foo",_time1,_time2);
		
		UpdateCommand update = new UpdateCommand(0);
		update.setTaskTitleToUpdate("bar");
		update.execute();
		
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		Task expectedTask = new Task("bar",_time1,_time2);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}
	
	@Test
	public void testDeleteCommandTaskName() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
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
	public void testDeleteCommandStartDateTime() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		addTask("foo",_time1,_time2);
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		DeleteCommand delete = new DeleteCommand(1);
		delete.setToDeleteStartDate();
		delete.execute();
		Task expectedTask = new Task("foo",_time2);
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	
	@Test
	public void testDeleteCommandStartEndDateTime() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		addTask("foo",_time1,_time2);
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		DeleteCommand delete = new DeleteCommand(1);
		delete.setToDeleteStartDate();
		delete.setToDeleteEndDate();
		delete.execute();
		Task expectedTask = new Task("foo");
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
		
	}
	
	
	@Test
	public void testDeleteCommandEndDateTime() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		addTask("foo",_time1,_time2);
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		DeleteCommand delete = new DeleteCommand(1);
		delete.setToDeleteEndDate();
		delete.execute();
		Task expectedTask = new Task("foo",_time1);
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	
	@Test
	public void testDeleteCommandStartEndTime() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		addTask("foo",_time1,_time2);
		_controller.getUI().display(_controller.getStorage().getTaskList().toArray(new Task[1]));
		DeleteCommand delete = new DeleteCommand(1);
		delete.setToDeleteStartTime();
		delete.setToDeleteEndTime();
		delete.execute();
		LocalDate startDate= _time1.toLocalDate();
		LocalDate endDate = _time2.toLocalDate();
		Task expectedTask = new Task("foo",startDate,endDate);
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
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
	public void testUndoAndRedoCommandSingleStep() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
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
	@Test
	public void testUndoAndRedoCommandMultipleSteps() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		AddCommand cmd = new AddCommand("foo",null,null);
		_controller.executeCommand(cmd);
		cmd =  new AddCommand("bar",null,null);
		_controller.executeCommand(cmd);
		UndoCommand undo = new UndoCommand(2);
		undo.execute();
		ArrayList<Task> list = _controller.getStorage().getTaskList();
		assertEquals(0,list.size());
		RedoCommand redo = new RedoCommand(2);
		redo.execute();
		list = _controller.getStorage().getTaskList();
		assertEquals(2,list.size());
	}

	
	//@author A0121555M
	@Test
	public void testDirectoryCommand() throws IOException{
		Path pathStr = _controller.getStorage().getStorageLocation();
		Path databaseLocation = pathStr.resolve("taskie.txt");
		Path newPathStr = pathStr.resolve("taskie_test_folder");
		Path newDatabaseLocation = newPathStr.resolve("taskie.txt");
		Files.createDirectory(newPathStr);
		DirectoryCommand cmd = new DirectoryCommand(newPathStr.toString(), false);
		cmd.execute();
		
		if ( !Files.isRegularFile(newDatabaseLocation) ) {
			fail("Failed to migrate file to " + newDatabaseLocation);
		}

		assertEquals(newPathStr.toString(), _controller.getStorage().getStorageLocation().toString());

		// Migrate Back
		cmd = new DirectoryCommand(pathStr.toString(), false);
		cmd.execute();

		if ( !Files.isRegularFile(databaseLocation) ) {
			fail("Failed to migrate file back to to " + databaseLocation);
		}
		Files.deleteIfExists(newPathStr);
		assertEquals(pathStr.toString(), _controller.getStorage().getStorageLocation().toString());
	}
	
	//@author A0097582N
	private void addTask(String taskName,LocalDateTime startDateTime, LocalDateTime endDateTime) {
		AddCommand cmd = new AddCommand();
		cmd.setTaskName(taskName);
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
				cmd.setEndDateTime(_time2.plusHours(i*3));
				if (i % 3 == 0) {
					cmd.setStartDateTime(_time1.plusHours(i*3));
				}
			}
			cmd.execute();
		}
		
	}

}
