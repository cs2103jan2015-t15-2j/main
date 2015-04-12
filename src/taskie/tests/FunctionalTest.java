//@author A0097582N
package taskie.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.After;
import org.junit.Before;
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
	
	@After
	public void tearDownAfterClass() throws Exception {
		_controller.getStorage().clearAllTasks();
	}
	@Before
	public void setUp() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		_controller = Controller.getInstance();
		_controller.getStorage().clearAllTasks();
		
	}

	@Test
	public void testAddFloating() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		ICommand cmd = _controller.getParser().parse("add taskname");
		cmd.execute();
		Task expectedTask = new Task("taskname");
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	@Test
	public void testAddDeadline() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
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
	//@author A0135137L
	@Test
	public void testDeleteFloating() throws TaskRetrievalFailedException {
		
		ICommand addCmd = null;
		ICommand displayCmd = null;
		ICommand delCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname");
			displayCmd = _controller.getParser().parse("display");
			delCmd = _controller.getParser().parse("delete 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		addCmd.execute();
		displayCmd.execute();
		delCmd.execute();
		assertEquals("[]",_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testDeleteDeadline() throws TaskRetrievalFailedException {
		
		ICommand addCmd = null;
		ICommand displayCmd = null;
		ICommand delCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname by 10pm");
			displayCmd = _controller.getParser().parse("display");
			delCmd = _controller.getParser().parse("delete 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		addCmd.execute();
		displayCmd.execute();
		delCmd.execute();
		assertEquals("[]",_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testDeleteTimed() throws TaskRetrievalFailedException {
		
		ICommand addCmd = null;
		ICommand displayCmd = null;
		ICommand delCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			displayCmd = _controller.getParser().parse("display");
			delCmd = _controller.getParser().parse("delete 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		addCmd.execute();
		displayCmd.execute();
		delCmd.execute();
		assertEquals("[]",_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testDisplay() throws TaskRetrievalFailedException {
		
		ICommand addCmd1 = null;		
		ICommand addCmd2 = null;
		ICommand addCmd3 = null;
		
		try {
			addCmd1 = _controller.getParser().parse("add taskname");
			addCmd2 = _controller.getParser().parse("add taskname by 10pm");
			addCmd3 = _controller.getParser().parse("add taskname from 2pm to 10pm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		addCmd1.execute();
		addCmd2.execute();
		addCmd3.execute();
		
		Task expectedTask1 = new Task("taskname");
		Task expectedTask2 = new Task("taskname",_today10pm);
		Task expectedTask3 = new Task("taskname",_today2pm,_today10pm);
		
		assertEquals("[" + expectedTask1.toString() + ", " + expectedTask2.toString()
				+ ", " + expectedTask3.toString() + "]", 
				_controller.getStorage().getTaskList().toString());
	}
	

}
