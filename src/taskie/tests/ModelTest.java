package taskie.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import taskie.exceptions.TaskDateInvalidException;
import taskie.exceptions.TaskDateNotSetException;
import taskie.models.Task;

@RunWith(value = Parameterized.class)
public class ModelTest {
	private static final String TASK_0_NAME = "Test - Time before Current"; // e.g. 28/3/2015 11.15am to 30/3/2015 9.15am
	private static final String TASK_1_NAME = "Test - Time after Current"; // e.g. 28/3/2015 11.15am to 30/3/2015 1.15pm
	private static final String TASK_2_NAME = "Test - Time equal Current"; // e.g. 28/3/2015 11.15am to 30/3/2015 11.15am
	private static final String TASK_3_NAME = "Test - Same day, time after current"; // e.g. 28/3/2015 11.15am to 28/3/2015 1.15pm
	private static final String TASK_4_NAME = "Test - Same day and time"; // e.g. 28/3/2015 11.15am to 28/3/2015 11.15am
	private static final String TASK_5_NAME = "Test - Deadlined Task"; // e.g. null to 30/3/2015 11.15am 
	private static final String TASK_6_NAME = "Test - Task with only date"; // e.g. null to 30/3/2015
	private static final String TASK_7_NAME = "Test - Deadlined Task with only date"; // e.g. null to 30/3/2015
	private static final String TASK_8_NAME = "Test - End Date before midnight"; // e.g. 30/3/2015 11.15pm to 30/3/2015 11.45pm
	private static final String TASK_9_NAME = "Test - End Date after midnight"; // e.g. 31/3/2015 1.15am to 31/3/2015 1.45am
	
	private static LocalDateTime _now;

	private Task task;
	private String _name;
	private LocalDateTime _start;
	private LocalDateTime _end;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_now = LocalDateTime.of(2015, 3, 28, 11, 15, 00);
	}

	@Parameters(name = "{index}: model({0}, start: {1}, end: {2})")
	public static Iterable<Object[]> dataset() throws Exception {
		setUpBeforeClass();
		return Arrays.asList(new Object[][] { 
				{ TASK_0_NAME, _now, _now.plusDays(2).plusHours(-2), false },
				{ TASK_1_NAME, _now, _now.plusDays(2).plusHours(2), false }, 
				{ TASK_2_NAME, _now, _now.plusDays(2), false }, 
				{ TASK_3_NAME, _now, _now.plusHours(2), false }, 
				{ TASK_4_NAME, _now, _now, false }, 
				{ TASK_5_NAME, null, _now.plusDays(2), false },
				{ TASK_6_NAME, _now, _now.plusDays(2), true },
				{ TASK_7_NAME, null, _now.plusDays(2), true },
				{ TASK_8_NAME, _now.plusHours(12), _now.plusHours(12).plusMinutes(30), false }, 
				{ TASK_9_NAME, _now.plusHours(13), _now.plusHours(13).plusMinutes(30), false }, 
			});
	}
	
	public ModelTest(String name, LocalDateTime start, LocalDateTime end, boolean isOnlyDate) throws Exception {
		_name = name;
		
		if ( isOnlyDate ) {
			if ( start == null ) {
				_start = null;
			} else {
				_start = start.with(LocalTime.MAX);
			}
			_end = end.with(LocalTime.MAX);
		} else {
			_start = start;
			_end = end;
		}
		
		if ( isOnlyDate ) {
			if ( start == null ) {
				task = new Task(name, _end.toLocalDate());
			} else {
				task = new Task(name, _start.toLocalDate(), _end.toLocalDate());
			}
		} else {
			if ( start == null ) {
				task = new Task(name, _end.toLocalDate(), _end.toLocalTime());
			} else {
				task = new Task(name, _start.toLocalDate(),_start.toLocalTime(), _end.toLocalDate(), _end.toLocalTime());
			}
		}
	}
	
	
	@Test
	public void testGetTitle() {
		assertEquals(_name, task.getTitle());
	}

	@Test
	public void testSetTitle() {
		task.setTitle("New Task Name");
		assertEquals("New Task Name", task.getTitle());
	}

	@Test
	public void testGetStartDateTime() {
		assertEquals(_start, task.getStartDateTime());
	}

	@Test
	public void testSetStartDateTimeAfterEnd() {
		// Test Setting Start Date Time after end
		LocalDateTime input = task.getEndDateTime().plusDays(1); // 1 day after end date time
		LocalDateTime expectedStart = task.getEndDateTime().plusDays(1);
		long daysDifference = task.getStartDate() == null ? 0 :Period.between(task.getStartDate(), expectedStart.toLocalDate()).getDays();
		long timeDifference = task.getStartTime() == null ? 0 :ChronoUnit.NANOS.between(task.getStartTime(), expectedStart.toLocalTime());
		LocalDateTime expectedEnd = task.getEndDateTime().plusDays(daysDifference).plusNanos(timeDifference);
		
		try {
			task.setStartDateTime(input);
			assertEquals(expectedStart, task.getStartDateTime());
			assertEquals(expectedEnd, task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetStartDateTimeBeforeEnd() {
		// Test Setting Start Date Time before end
		LocalDateTime input = task.getEndDateTime().plusDays(-1); // 1 day before end date time
		LocalDateTime expectedStart = task.getEndDateTime().plusDays(-1);
		long daysDifference = task.getStartDate() == null ? 0 : Period.between(task.getStartDate(), expectedStart.toLocalDate()).getDays();
		long timeDifference = task.getStartTime() == null ? 0 : ChronoUnit.NANOS.between(task.getStartTime(), expectedStart.toLocalTime());
		LocalDateTime expectedEnd = task.getEndDateTime().plusDays(daysDifference).plusNanos(timeDifference);
		
		try {
			task.setStartDateTime(input);
			assertEquals(expectedStart, task.getStartDateTime());
			assertEquals(expectedEnd, task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetStartDateTimeEqualsEnd() {
		// Test Setting Start Date Time equals end
		LocalDateTime input = task.getEndDateTime(); // set same as end date time
		LocalDateTime expectedStart = task.getEndDateTime();
		long daysDifference = task.getStartDate() == null ? 0 : Period.between(task.getStartDate(), expectedStart.toLocalDate()).getDays();
		long timeDifference = task.getStartTime() == null ? 0 : ChronoUnit.NANOS.between(task.getStartTime(), expectedStart.toLocalTime());
		LocalDateTime expectedEnd = task.getEndDateTime().plusDays(daysDifference).plusNanos(timeDifference);
		
		try {
			task.setStartDateTime(input);
			assertEquals(expectedStart, task.getStartDateTime());
			assertEquals(expectedEnd, task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}

	@Test
	public void testGetEndDateTime() {
		assertEquals(_end, task.getEndDateTime());
	}

	@Test
	public void testSetEndDateTimeAfterStart() {
		// Test Setting End Date Time after start
		if ( task.getStartDate() == null ) {
			return;
		}
		
		LocalDateTime input = task.getStartDateTime().plusDays(1);
		LocalDateTime expectedStart = task.getStartDateTime();
		LocalDateTime expectedEnd = task.getStartDateTime().plusDays(1);
		
		try {
			task.setEndDateTime(input);
			assertEquals(expectedStart, task.getStartDateTime());
			assertEquals(expectedEnd, task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw exception: " + task.getStartDateTime() + " after " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetEndDateTimeBeforeStart() {
		// Test Setting End Date Time before start
		if ( task.getStartDate() == null ) {
			return;
		}

		LocalDateTime input = task.getStartDateTime().plusDays(-1);
		LocalDateTime expectedStart = task.getStartDateTime();
		LocalDateTime expectedEnd = task.getEndDateTime();
		
		try {
			task.setEndDateTime(input);
			fail("Did not throw exception: " + task.getStartDateTime() + " after " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			assertEquals(expectedStart, task.getStartDateTime());
			assertEquals(expectedEnd, task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetEndDateTimeEqualsStart() {
		// Test Setting End Date Time same as start
		if ( task.getStartDate() == null ) {
			return;
		}
		
		LocalDateTime input = task.getStartDateTime();
		LocalDateTime expectedStart = task.getStartDateTime();
		LocalDateTime expectedEnd = task.getStartDateTime();
		
		try {
			task.setEndDateTime(input);
			assertEquals(expectedStart, task.getStartDateTime());
			assertEquals(expectedEnd, task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}

	@Test
	public void testGetStartDate() {
		if ( _name.equals(TASK_5_NAME) || _name.equals(TASK_7_NAME) ) {
			assertEquals(null, task.getStartDate());
		} else {
			assertEquals(_start.toLocalDate(), task.getStartDate());
		}
	}

	@Test
	public void testSetStartDateAfterEnd() {
		// Test Setting Start Date after End
		LocalDate input = task.getEndDate().plusDays(1);
		LocalDate expectedStart = task.getEndDate().plusDays(1);
		long daysDifference = task.getStartDate() == null ? 0 : Period.between(task.getStartDate(), expectedStart).getDays();
		LocalDate expectedEnd = task.getEndDate().plusDays(daysDifference);
		
		try {
			task.setStartDateTime(input, task.getStartTime());
			assertEquals(expectedStart, task.getStartDate());
			assertEquals(expectedEnd, task.getEndDate());
		} catch (TaskDateInvalidException e) {
			if ( !_name.equals(TASK_0_NAME) ) {
				fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}

	@Test
	public void testSetStartDateBeforeEnd() {
		// Test Setting Start Date before End
		LocalDate input = task.getEndDate().plusDays(-1);
		LocalDate expectedStart = task.getEndDate().plusDays(-1);
		long daysDifference = task.getStartDate() ==  null ? 0 : Period.between(task.getStartDate(), expectedStart).getDays();
		LocalDate expectedEnd = task.getEndDate().plusDays(daysDifference);
		
		try {
			task.setStartDateTime(input, task.getStartTime());
			assertEquals(expectedStart, task.getStartDate());
			assertEquals(expectedEnd, task.getEndDate());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetStartDateEqualsEnd() {
		// Test Setting Start Date equals End
		LocalDate input = task.getEndDate();
		LocalDate expectedStart = task.getEndDate();
		long daysDifference = task.getStartDate() == null ? 0 : Period.between(task.getStartDate(), expectedStart).getDays();
		LocalDate expectedEnd = task.getEndDate().plusDays(daysDifference);
		
		try {
			task.setStartDateTime(input, task.getStartTime());
			assertEquals(expectedStart, task.getStartDate());
			assertEquals(expectedEnd, task.getEndDate());
		} catch (TaskDateInvalidException e) {
			if ( !_name.equals(TASK_0_NAME) ) {
				fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testGetEndDate() {
		assertEquals(_end.toLocalDate(), task.getEndDate());
	}

	@Test
	public void testSetEndDateAfterStart() {
		// Test Setting End Date after Start
		if ( task.getStartDate() == null ) {
			return;
		}
	
		LocalDate input = task.getStartDate().plusDays(1);
		LocalDate expectedStart = task.getStartDate();
		LocalDate expectedEnd = task.getStartDate().plusDays(1);
		
		try {
			task.setEndDateTime(input, task.getEndTime());
			assertEquals(expectedStart, task.getStartDate());
			assertEquals(expectedEnd, task.getEndDate());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}

	@Test
	public void testSetEndDateBeforeStart() {
		// Test Setting End Date before Start
		if ( task.getStartDate() == null ) {
			return;
		}
	
		LocalDate input = task.getStartDate().plusDays(-1);
		LocalDate expectedStart = task.getStartDate();
		LocalDate expectedEnd = task.getEndDate();
		
		try {
			task.setEndDateTime(input, task.getEndTime());
			fail("Did not throw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateInvalidException e) {
			assertEquals(expectedStart, task.getStartDate());
			assertEquals(expectedEnd, task.getEndDate());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetEndDateEqualsStart() {
		// Test Setting End Date equals Start
		if ( task.getStartDate() == null ) {
			return;
		}
		
		LocalDate input = task.getStartDate();
		LocalDate expectedStart = task.getStartDate();
		LocalDate expectedEnd = task.getStartDate();
		
		try {
			task.setEndDateTime(input, task.getEndTime());
			assertEquals(expectedStart, task.getStartDate());
			assertEquals(expectedEnd, task.getEndDate());
		} catch (TaskDateInvalidException e) {
			if ( !_name.equals(TASK_0_NAME) ) {
				fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}

	@Test
	public void testGetStartTime() {
		if ( _name.equals(TASK_5_NAME) || _name.equals(TASK_7_NAME) ) {
			assertEquals(null, task.getStartTime());
		} else {
			assertEquals(_start.toLocalTime(), task.getStartTime() == null ? LocalTime.MAX : task.getStartTime());
		}
	}

	@Test
	public void testSetStartTimeAfterEnd() {
		// Test Setting Start Time after End
		LocalTime input = task.getEndTime() == null ? null : task.getEndTime().plusHours(1);
		LocalTime expectedStart = task.getEndTime() == null ? null : task.getEndTime().plusHours(1);
		long timeDifference = task.getStartTime() == null ? 0 : ChronoUnit.NANOS.between(task.getStartTime(), expectedStart);
		LocalTime expectedEnd = task.getEndTime() == null ? null : task.getEndTime().plusNanos(timeDifference);
		
		try {
			task.setStartDateTime(task.getStartDate(), input);
			assertEquals(expectedStart, task.getStartTime());
			assertEquals(expectedEnd, task.getEndTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			if ( !_name.equals(TASK_5_NAME) ) {
				fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		}
	}
	
	@Test
	public void testSetStartTimeBeforeEnd() {
		// Test Setting Start Time before End
		LocalTime input = task.getEndTime() == null ? null : task.getEndTime().plusHours(-1);
		LocalTime expectedStart = task.getEndTime() == null ? null : task.getEndTime().plusHours(-1);
		long timeDifference = task.getStartTime() == null ? 0 : ChronoUnit.NANOS.between(task.getStartTime(), expectedStart);
		LocalTime expectedEnd = task.getEndTime() == null ? null : task.getEndTime().plusNanos(timeDifference);
		
		try {
			task.setStartDateTime(task.getStartDate(), input);
			assertEquals(expectedStart, task.getStartTime());
			assertEquals(expectedEnd, task.getEndTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			if ( !_name.equals(TASK_5_NAME) ) {
				fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		}
	}
	
	@Test
	public void testSetStartTimeEqualEnd() {
		// Test Setting Start Time equal End
		LocalTime input = task.getEndTime() == null ? null : task.getEndTime();
		LocalTime expectedStart = task.getEndTime() == null ? null : task.getEndTime();
		long timeDifference = task.getStartTime() == null ? 0 : ChronoUnit.NANOS.between(task.getStartTime(), expectedStart);
		LocalTime expectedEnd = task.getEndTime() == null ? null : task.getEndTime().plusNanos(timeDifference);
		
		try {
			task.setStartDateTime(task.getStartDate(), input);
			assertEquals(expectedStart, task.getStartTime());
			assertEquals(expectedEnd, task.getEndTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			if ( !_name.equals(TASK_5_NAME) ) {
				fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		}
	}

	@Test
	public void testGetEndTime() {
		assertEquals(_end.toLocalTime(), task.getEndTime() == null ? LocalTime.MAX : task.getEndTime());
	}

	@Test
	public void testSetEndTimeAfterStart() {
		// Test Setting End Time after Start		
		LocalTime input = task.getStartTime() == null ? null : task.getStartTime().plusHours(1);
		LocalTime expectedStart = task.getStartTime() == null ? null : task.getStartTime();
		LocalTime expectedEnd;
		if ( _name.equals(TASK_8_NAME) ) {
			expectedEnd = task.getEndTime(); // No Change
		} else {
			expectedEnd = task.getStartTime() == null ? null : task.getStartTime().plusHours(1);
		}
		
		try {
			task.setEndDateTime(task.getEndDate(), input);
			assertEquals(expectedStart, task.getStartTime());
			assertEquals(expectedEnd, task.getEndTime());
		} catch (TaskDateInvalidException e) {
			if ( _name.equals(TASK_8_NAME) ) {
				assertEquals(expectedStart, task.getStartTime());
				assertEquals(expectedEnd, task.getEndTime());
			} else {
				fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetEndTimeBeforeStart() {
		// Test Setting End Time before Start		
		LocalTime input = task.getStartTime() == null ? null : task.getStartTime().plusHours(-1);
		LocalTime expectedStart = task.getStartTime() == null ? null : task.getStartTime();
		LocalTime expectedEnd;
		if ( _name.equals(TASK_3_NAME) || _name.equals(TASK_4_NAME) || _name.equals(TASK_8_NAME) ) {
			expectedEnd = task.getEndTime();
		} else {
			expectedEnd = task.getStartTime() == null ? null : task.getStartTime().plusHours(-1);
		}
		
		try {
			task.setEndDateTime(task.getEndDate(), input);
			if ( _name.equals(TASK_3_NAME) || _name.equals(TASK_4_NAME) || _name.equals(TASK_8_NAME) ) {
				fail("Did not throw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			} else {
				assertEquals(expectedStart, task.getStartTime());
				assertEquals(expectedEnd, task.getEndTime());
			}
		} catch (TaskDateInvalidException e) {
			if ( _name.equals(TASK_3_NAME) || _name.equals(TASK_4_NAME) || _name.equals(TASK_8_NAME) ) {
				assertEquals(expectedStart, task.getStartTime());
				assertEquals(expectedEnd, task.getEndTime());
			} else {
				fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
			}
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}
	
	@Test
	public void testSetEndTimeEqualStart() {
		// Test Setting End Time equal Start
		try {
			task.setEndDateTime(task.getEndDate(), task.getStartTime());
			assertEquals(_start, task.getStartDateTime());
		} catch (TaskDateInvalidException e) {
			fail("Threw TaskDateInvalidException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		} catch (TaskDateNotSetException e) {
			fail("Threw TaskDateNotSetException: " + task.getStartDateTime() + " before " + task.getEndDateTime());
		}
	}

	@Test
	public void testSetTaskDone() {
		task.setTaskDone();
		assertEquals(true, task.isDone());
	}

	@Test
	public void testSetTaskUndone() {
		task.setTaskUndone();
		assertEquals(false, task.isDone());
	}

	@Test
	public void testIsDone() {
		assertEquals(false, task.isDone());
	}

	@Test
	public void testGetTaskType() {
	}

	@Test
	public void testCompareToTask() {
	}

	@Test
	public void testCompareToLocalDateTime() {
	}

	@Test
	public void testEqualsTask() {
	}

}
