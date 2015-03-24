package taskie.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.commands.AddCommand;
import taskie.commands.DeleteCommand;
import taskie.commands.DirectoryCommand;
import taskie.commands.ExitCommand;
import taskie.commands.ICommand;
import taskie.commands.MarkCommand;
import taskie.commands.RedoCommand;
import taskie.commands.UndoCommand;
import taskie.commands.UnmarkCommand;
import taskie.commands.ViewCommand;
import taskie.exceptions.InvalidCommandException;
import taskie.models.ViewType;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

import com.joestelmach.natty.CalendarSource;

public class ParserTest {
	private static final LocalDateTime MIN_DATETIME = LocalDateTime.MIN;
	private static final LocalDateTime MAX_DATETIME = LocalDateTime.MAX;

	private static Parser _parser;

	private static LocalDate _today;
	private static LocalTime _time;
	private static LocalDateTime _now;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_parser = new CommandParser();
		_now = LocalDateTime.now();
		
		_today = _now.toLocalDate();
		_time = _now.toLocalTime();

		Instant instant = _now.atZone(ZoneId.systemDefault()).toInstant();
		CalendarSource.setBaseDate(Date.from(instant));
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAddCommand() throws InvalidCommandException {
		AddCommand expectedCommand;
		ICommand actualCommand;
		
		// Test Floating Tasks
		expectedCommand = new AddCommand("Travel to London", null, null);
		actualCommand = _parser.parse("put Travel to London");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("create Travel to London");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("ins Travel to London");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Eat fries", null, null);
		actualCommand = _parser.parse("create Eat fries");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		// Test Deadlined Tasks with Relative Dates
		expectedCommand = new AddCommand("Do CE3", null, null, _today.plusDays(1), LocalTime.MAX);
		actualCommand = _parser.parse("add Do CE3 by tomorrow");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		expectedCommand = new AddCommand("Do CE3", null,  _now.plusDays(1));
		actualCommand = _parser.parse("add Do CE3 in 24 hours");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Have dinner", null, _now.plusHours(2));
		actualCommand = _parser.parse("add Have dinner in 2 hours");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("add Have dinner in 120 minutes");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Complete CE2", null, null, _today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)), LocalTime.MAX);
		actualCommand = _parser.parse("put Complete CE2 on Saturday");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Work on the code done by Amy", null, null, _today.with(TemporalAdjusters.next(DayOfWeek.MONDAY)), LocalTime.MAX);
		actualCommand = _parser.parse("create Work on the code done by Amy by Monday");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Reach Changi Airport to send friend off", null, null, _today.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)), LocalTime.of(20, 45));
		actualCommand = _parser.parse("add Reach Changi Airport to send friend off by 8.45pm thursday");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Eat fries", null, null, _today.with(TemporalAdjusters.next(DayOfWeek.THURSDAY)), LocalTime.of(20, 30));
		actualCommand = _parser.parse("create Eat fries at 8.30pm thurs");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		// Test Next Week
		expectedCommand = new AddCommand("Complete CS2103 Tutorial 5", null, null, _today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY)), LocalTime.MAX);
		actualCommand = _parser.parse("create Complete CS2103 Tutorial 5 by next Wednesday");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Complete next Wednesday's task", null, null, _today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY)), null);
		actualCommand = _parser.parse("ins Complete next Wednesday's task by next Tuesday");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		// Test Mix with "Public Holidays"
		expectedCommand = new AddCommand("Buy groceries for new year", null, null, _today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)), null);
		actualCommand = _parser.parse("add Buy groceries by saturday for new year");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		// Test Deadlined Tasks with Fixed Dates
		expectedCommand = new AddCommand("CS2103 Presentation", null, null, LocalDate.of(_today.getYear(), 4, 10), LocalTime.of(10,12));
		actualCommand = _parser.parse("create CS2103 Presentation on 10 April 10:12am");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Add the create task functionality for CS2103", null, null, LocalDate.of(_today.getYear(), 3, 15), LocalTime.MAX);
		actualCommand = _parser.parse("create Add the create task functionality for CS2103 by 15 March");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Presentation for CS2103", null, null, LocalDate.of(_today.getYear(), 4, 10), LocalTime.of(10,12));
		actualCommand = _parser.parse("create Presentation for CS2103 on 10 April 10.12am");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		// Test Timed Tasks with Relative Dates
		expectedCommand = new AddCommand("Attend CS2103 Meeting", _today.plusDays(1), LocalTime.of(10, 0), _today.plusDays(1), LocalTime.of(18, 0));
		actualCommand = _parser.parse("add Attend CS2103 Meeting from tomorrow 10am to 6pm");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Prepare Cousin Wedding", _today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)), LocalTime.of(10, 0), _today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)), LocalTime.of(18, 0));
		actualCommand = _parser.parse("add Prepare Cousin Wedding from saturday 10am to sunday 6pm");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		expectedCommand = new AddCommand("Do CS2103 Tutorial 6", _today.plusDays(1), LocalTime.of(9, 0), _today.plusDays(1), LocalTime.of(11, 0));
		actualCommand = _parser.parse("add Do CS2103 Tutorial 6 from 9 to 11am tomorrow");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new AddCommand("Dinner Date", _today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)), LocalTime.of(17, 0), _today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)), LocalTime.of(22, 0));
		actualCommand = _parser.parse("ins Dinner Date on Friday 5pm to 10pm");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		expectedCommand = new AddCommand("Reunion Dinner", _today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)), LocalTime.of(17, 45), _today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)), LocalTime.of(21, 15));
		actualCommand = _parser.parse("ins Reunion Dinner on Saturday 5.45pm to 9.15pm");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
	}
	
	@Test
	public void testViewCommand() throws InvalidCommandException {
		ViewCommand expectedCommand;
		ICommand actualCommand;
		
		// Test all view
		expectedCommand = new ViewCommand(ViewType.ALL, null, null, null, null, null);
		actualCommand = _parser.parse("display");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new ViewCommand(ViewType.ALL, _today.plusWeeks(1), null, MAX_DATETIME.toLocalDate(), MAX_DATETIME.toLocalTime(), null);
		actualCommand = _parser.parse("view all after next week");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		// Test overdue view
		expectedCommand = new ViewCommand(ViewType.OVERDUE, null, null, null, null, null);
		actualCommand = _parser.parse("view overdue");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		// Test upcoming view
		expectedCommand = new ViewCommand(ViewType.UPCOMING, null, null, null, null, null);
		actualCommand = _parser.parse("view todo");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		// Test search view
		expectedCommand = new ViewCommand(ViewType.SEARCH, null, null, null, null, "dinner");
		actualCommand = _parser.parse("find dinner");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("search dinner");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new ViewCommand(ViewType.SEARCH, null, null, null, null, "CS2103 tutorial".toLowerCase());
		actualCommand = _parser.parse("find CS2103 tutorial");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
	}
	
	@Test
	public void testDeleteCommand() throws InvalidCommandException {
		DeleteCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new DeleteCommand(10);
		actualCommand = _parser.parse("delete 10");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("clear 10");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("remove 10");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("rm 10");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("discard 10");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		try {
			actualCommand = _parser.parse("delete invalidstring");
			fail();
		} catch ( InvalidCommandException e ) {
		}
	}
	
	@Test
	public void testUpdateCommand() throws InvalidCommandException {
		
	}
	
	@Test
	public void testUndoCommand() throws InvalidCommandException {
		UndoCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new UndoCommand(1);
		actualCommand = _parser.parse("undo");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("revert");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new UndoCommand(5);
		actualCommand = _parser.parse("undo 5");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		try {
			expectedCommand = new UndoCommand(1);
			actualCommand = _parser.parse("undo invalidstring");
			assertEquals(expectedCommand.toString(), actualCommand.toString());
		} catch ( InvalidCommandException e ) {
		}
	}
	
	@Test
	public void testRedoCommand() throws InvalidCommandException {
		RedoCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new RedoCommand(1);
		actualCommand = _parser.parse("redo");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		expectedCommand = new RedoCommand(5);
		actualCommand = _parser.parse("redo 5");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		
		try {
			expectedCommand = new RedoCommand(1);
			actualCommand = _parser.parse("redo invalidstring");
			assertEquals(expectedCommand.toString(), actualCommand.toString());
		} catch ( InvalidCommandException e ) {
		}
	}
	
	@Test
	public void testMarkCommand() throws InvalidCommandException {
		MarkCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new MarkCommand(1);
		actualCommand = _parser.parse("mark 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("complete 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("done 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("check 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		try {
			actualCommand = _parser.parse("mark");
			fail();
		} catch ( InvalidCommandException e ) {
		}
		
		try {
			actualCommand = _parser.parse("mark invalidstring");
			fail();
		} catch ( InvalidCommandException e ) {
		}
	}
	
	@Test
	public void testUnmarkCommand() throws InvalidCommandException {
		UnmarkCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new UnmarkCommand(1);
		actualCommand = _parser.parse("unmark 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("incomplete 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("undone 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("uncheck 1");
		assertEquals(expectedCommand.toString(), actualCommand.toString());

		try {
			actualCommand = _parser.parse("unmark");
			fail();
		} catch ( InvalidCommandException e ) {
		}
		
		try {
			actualCommand = _parser.parse("unmark invalidstring");
			fail();
		} catch ( InvalidCommandException e ) {
		}
	}
	
	@Test
	public void testDirectoryCommand() throws InvalidCommandException {
		DirectoryCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new DirectoryCommand();
		actualCommand = _parser.parse("directory");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
	}

	@Test
	public void testExitCommand() throws InvalidCommandException {
		ExitCommand expectedCommand;
		ICommand actualCommand;
		
		expectedCommand = new ExitCommand();
		actualCommand = _parser.parse("exit");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("quit");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
		actualCommand = _parser.parse("close");
		assertEquals(expectedCommand.toString(), actualCommand.toString());
	}
}
