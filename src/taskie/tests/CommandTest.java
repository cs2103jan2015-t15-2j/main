//@author A0121555M
package taskie.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

	private static LocalDate _evenLaterDate;
	private static LocalTime _evenLaterTime;

	private static LocalDateTime _now;
	private static LocalDateTime _later;
	private static LocalDateTime _evenLater;

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
		_parser = new CommandParser();
		_now = LocalDateTime.now();
		_later = LocalDateTime.now();
		_evenLater = LocalDateTime.now();
		_time1 = LocalDateTime.of(2100,1,1,12,0).plusDays(1).plusHours(1);
		_time2 = _time1.plusHours(2);
		_time3 = _time1.plusDays(1);

		
		_nowDate = _now.toLocalDate();
		_nowTime = _now.toLocalTime();
		_laterDate = _later.toLocalDate();
		_laterTime = _later.toLocalTime();
		_evenLaterDate = _evenLater.toLocalDate();
		_evenLaterTime = _evenLater.toLocalTime();
		_time1Date = _time1.toLocalDate();
		_time1Time = _time1.toLocalTime();
		_time2Date = _time2.toLocalDate();
		_time2Time = _time2.toLocalTime();
		_time3Date = _time3.toLocalDate();
		_time3Time = _time3.toLocalTime();

		Instant instant = _now.atZone(ZoneId.systemDefault()).toInstant();
		CalendarSource.setBaseDate(Date.from(instant));
	}

	@AfterClass
	public static void cleanUp() throws IOException,
			TaskRetrievalFailedException {
		Taskie.Controller = new taskie.controller.Controller();
		Taskie.Controller.getStorage().deleteTaskList();
	}

	@Before
	public void setUp() throws TaskRetrievalFailedException, IOException {
		Taskie.Controller = new taskie.controller.Controller();
		Taskie.Controller.getStorage().deleteTaskList();
	}

	// @Test
	public void testAddCommandFloating() throws InvalidCommandException,
			InvalidTaskException, TaskRetrievalFailedException, IOException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		Taskie.Controller.executeCommand(cmd);
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo");
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	// @Test
	public void testAddCommandDeadline() throws TaskRetrievalFailedException,
			IOException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("bar");
		cmd.setEndDateTime(_now);
		Taskie.Controller.executeCommand(cmd);
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("bar", _nowDate, _nowTime);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	// @Test
	public void testAddCommandTimed() throws TaskRetrievalFailedException,
			IOException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foobar");
		cmd.setEndDateTime(_later);
		cmd.setStartDateTime(_now);
		Taskie.Controller.executeCommand(cmd);
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foobar", _nowDate, _nowTime, _laterDate,
				_laterTime);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	// @Test
	public void testViewCommandAll() throws TaskRetrievalFailedException,
			IOException {
		setUp();
		generateTasks();
		ViewCommand cmd = new ViewCommand(ViewType.ALL);
		cmd.execute();
	}

	// @Test
	public void testUpdateCommandTaskNameSimple()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.execute();
		ArrayList<Task> tasks = Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));

		UpdateCommand update = new UpdateCommand();
		update.setTaskTitleToUpdate("bar");
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("bar");
		assertEquals(expectedTask.toString(), list.get(0).toString());

	}

	// @Test
	public void testUpdateCommandTaskNameComplex()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.setStartDateTime(_now);
		cmd.setEndDateTime(_later);
		cmd.execute();
		ArrayList<Task> tasks = Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setTaskTitleToUpdate("bar");
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("bar");
		expectedTask.setStartDateTime(_now);
		expectedTask.setEndDateTime(_later);
		assertEquals(expectedTask.toString(), list.get(0).toString());

	}

	// @Test
	public void testUpdateCommandEndTime() throws TaskRetrievalFailedException,
			IOException, InvalidTaskException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.setEndDateTime(_now);
		cmd.execute();
		ArrayList<Task> tasks = Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setEndDateToUpdate(_laterDate);
		update.setEndTimeToUpdate(_laterTime);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo");
		expectedTask.setEndDateTime(_later);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	// @Test
	public void testUpdateCommandStartEndDateTime()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.setEndDateTime(_later);
		cmd.setStartDateTime(_now);
		cmd.execute();
		ArrayList<Task> tasks = Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setEndDateToUpdate(_evenLaterDate);
		update.setEndTimeToUpdate(_evenLaterTime);
		update.setStartDateToUpdate(_laterDate);
		update.setStartTimeToUpdate(_laterTime);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo");
		expectedTask.setStartDateTime(_later);
		expectedTask.setEndDateTime(_evenLater);
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	@Test
	public void testUpdateCommandStartEndDateTimeComplex()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.setEndDateTime(_time1);
		cmd.setStartDateTime(_now);
		cmd.execute();
		ArrayList<Task> tasks = Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setStartDateToUpdate(_time2Date);
		update.setStartTimeToUpdate(_time2Time);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo");

		long duration = _now.until(_time1, ChronoUnit.MINUTES);
		expectedTask.setStartDateTime(_time2);
		expectedTask.setEndDateTime(_time2.plusMinutes(duration));
		System.out.println(expectedTask);
		System.out.println(list.get(0).toString());
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}
	//startdate and enddate of task is 2 hours apart, updatecommand updates startdate to the nextday
	@Test
	public void testUpdateCommandStartEndDateTimeComplex2()
			throws TaskRetrievalFailedException, IOException,
			InvalidTaskException {
		setUp();
		AddCommand cmd = new AddCommand();
		cmd.setTaskName("foo");
		cmd.setEndDateTime(_time2);
		cmd.setStartDateTime(_time1);
		cmd.execute();
		ArrayList<Task> tasks = Taskie.Controller.getStorage().getTaskList();
		Taskie.Controller.getUI()
				.display(tasks.toArray(new Task[tasks.size()]));
		UpdateCommand update = new UpdateCommand();
		update.setStartDateToUpdate(_time3Date);
		update.setStartTimeToUpdate(_time3Time);
		update.setTaskIndex(1);
		update.execute();
		ArrayList<Task> list = Taskie.Controller.getStorage().getTaskList();
		Task expectedTask = new Task("foo");

		long duration = _time1.until(_time2, ChronoUnit.MINUTES);
		expectedTask.setStartDateTime(_time3);
		expectedTask.setEndDateTime(_time3.plusMinutes(duration));
		System.out.println(expectedTask);
		System.out.println(list.get(0).toString());
		assertEquals(expectedTask.toString(), list.get(0).toString());
	}

	private void generateTasks() {
		for (int i = 0; i < 10; i++) {
			AddCommand cmd = new AddCommand();
			cmd.setTaskName("foo " + i);
			if (i % 2 == 0) {
				cmd.setEndDateTime(_later);
				if (i % 3 == 0) {
					cmd.setStartDateTime(_now);
				}
			}
			cmd.execute();
		}
	}

}
