package taskie.tests;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.Taskie;
import taskie.commands.ICommand;
import taskie.controller.Controller;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.InvalidTaskException;
import taskie.models.Task;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

import com.joestelmach.natty.CalendarSource;

public class CommandTest {
	private static final LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static final LocalDateTime MAX_DATETIME = LocalDateTime.MAX;

	private static Parser _parser;

	private static LocalDate _today;
	private static LocalTime _time;
	private static LocalDateTime _now;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Taskie.Controller = new taskie.controller.Controller();
		
		_parser = new CommandParser();
		_now = LocalDateTime.now();
		
		_today = _now.toLocalDate();
		_time = _now.toLocalTime();

		Instant instant = _now.atZone(ZoneId.systemDefault()).toInstant();
		CalendarSource.setBaseDate(Date.from(instant));
	}
	
	@Before
	public void setUp() throws Exception {
		this.generateTasks();
	}

	@Test
	public void testAddCommand() throws InvalidCommandException, InvalidTaskException {
		ICommand cmd = _parser.parse("create work on Task 10 by 25 March 2015");
		Taskie.Controller.executeCommand(cmd);
		Task[] list = Taskie.Controller.getUI().getCurrentTaskList();
		
		Task expectedTask = new Task("work on Task 10", LocalDate.of(2015, 3, 25), MAX_DATETIME.toLocalTime());
		assertEquals(expectedTask.toString(), list[list.length - 1].toString());
	}
	
	private void generateTasks() {
		String[] tasks = new String[] {
				"add do Task A",
				"add do Task B by tomorrow",
				"put collect Task B in 48 hours",
				"create work on Task 1 on Monday",
				"create work on Task 2 on Tuesday",
				"create work on Task 3 on Wednesday",
				"create work on Task 4 on Thursday",
				"create work on Task 5 on Friday",
				"create work on Task 6 on Saturday",
				"create work on Task 7 on Sunday",
				"create work on Task 8 by " + _now.plusDays(3),
				"create work on Task 9 by " + _now.plusDays(4).plusHours(4),
		};
		
		for (String task : tasks) {
			try {
				ICommand cmd = _parser.parse(task);
				Taskie.Controller.executeCommand(cmd);
			} catch ( InvalidCommandException e ) {
				
			}			
		}
	}

}
