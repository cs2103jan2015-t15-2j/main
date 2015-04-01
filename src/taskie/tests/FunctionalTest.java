//@author A0097582N
package taskie.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.Controller;
import taskie.commands.ICommand;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.models.Task;

public class FunctionalTest {
	private static Controller _controller;
	
	LocalDateTime _now = LocalDateTime.now();
	LocalDateTime _today10pm = LocalDateTime.of(_now.toLocalDate(), LocalTime.of(22, 0, 0, 0));
	LocalDateTime _today2pm = LocalDateTime.of(_now.toLocalDate(), LocalTime.of(14,0,0,0));
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		_controller.getStorage().clearAllTasks();
	}
	@BeforeClass
	public static void setUp() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		_controller = Controller.getInstance();
		_controller.getStorage().clearAllTasks();
		
	}

	@Test
	public void testAddFloating() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		setUp();
		ICommand cmd = _controller.getParser().parse("add taskname");
		cmd.execute();
		Task expectedTask = new Task("taskname");
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	@Test
	public void testAddDeadline() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		ICommand cmd=null;
		
		try {
			cmd = _controller.getParser().parse("add taskname by 10pm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmd.execute();
		Task expectedTask = new Task("taskname",_today10pm);
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	@Test
	public void testAddTimed() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		setUp();
		ICommand cmd=null;
		
		try {
			cmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		cmd.execute();
		Task expectedTask = new Task("taskname",_today2pm,_today10pm);
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	

}
