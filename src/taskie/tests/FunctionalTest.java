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
import taskie.commands.Command;
import taskie.exceptions.InvalidCommandException;
import taskie.exceptions.TaskModificationFailedException;
import taskie.exceptions.TaskRetrievalFailedException;
import taskie.models.DateTimeSource;
import taskie.models.Task;

public class FunctionalTest {
	private static Controller _controller;
	
	LocalDateTime _now = LocalDateTime.now();
	LocalDateTime _today10pm = LocalDateTime.of(_now.toLocalDate(), LocalTime.of(22, 0, 0, 0));
	LocalDateTime _today2pm = LocalDateTime.of(_now.toLocalDate(), LocalTime.of(14,0,0,0));
	
	@After
	public void tearDownAfterClass() throws Exception {
		_controller.getStorage().clearAllTasks();
		_controller.resetUndoAndRedoStacks();
	}
	
	@Before
	public void setUp() throws TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		DateTimeSource.setCurrentDateTime(_now);
		_controller = Controller.getInstance();
		_controller.getStorage().clearAllTasks();
	}

	@Test
	public void testAddFloating() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException {
		Command cmd = _controller.getParser().parse("add taskname");
		_controller.executeCommand(cmd);
		Task expectedTask = new Task("taskname");
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	@Test
	public void testAddDeadline() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		Command cmd=null;
		
		try {
			cmd = _controller.getParser().parse("add taskname by 10pm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		_controller.executeCommand(cmd);
		Task expectedTask = null;
		if(LocalTime.now().isAfter(_today10pm.toLocalTime())){
			expectedTask = new Task("taskname", _today10pm.plusDays(1));
		}else{
			expectedTask = new Task("taskname",_today10pm);
		}
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	
	@Test
	public void testAddTimed() throws InvalidCommandException, TaskRetrievalFailedException, IOException, TaskModificationFailedException{
		Command cmd=null;
		
		try {
			cmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		_controller.executeCommand(cmd);
		Task expectedTask = null;
		if(LocalTime.now().isAfter(_today2pm.toLocalTime())){
			expectedTask = new Task("taskname",_today2pm.plusDays(1),_today10pm.plusDays(1));
		}else{
			expectedTask = new Task("taskname",_today2pm,_today10pm);
		}
		
		assertEquals(expectedTask.toString(),_controller.getStorage().getTaskList().get(0).toString());
	}
	
	//@author A0135137L
	@Test
	public void testDeleteFloating() throws TaskRetrievalFailedException {
		
		Command addCmd = null;
		Command displayCmd = null;
		Command delCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname");
			displayCmd = _controller.getParser().parse("display");
			delCmd = _controller.getParser().parse("delete 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(delCmd);
		assertEquals("[]",_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testDeleteDeadline() throws TaskRetrievalFailedException {
		
		Command addCmd = null;
		Command displayCmd = null;
		Command delCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname by 10pm");
			displayCmd = _controller.getParser().parse("display");
			delCmd = _controller.getParser().parse("delete 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(delCmd);
		assertEquals("[]",_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testDeleteTimed() throws TaskRetrievalFailedException {
		
		Command addCmd = null;
		Command displayCmd = null;
		Command delCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			displayCmd = _controller.getParser().parse("display");
			delCmd = _controller.getParser().parse("delete 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(delCmd);
		assertEquals("[]",_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testDisplay() throws TaskRetrievalFailedException {
		
		Command addCmd1 = null;		
		Command addCmd2 = null;
		Command addCmd3 = null;
		
		try {
			addCmd1 = _controller.getParser().parse("add taskname");
			addCmd2 = _controller.getParser().parse("add taskname by 10pm");
			addCmd3 = _controller.getParser().parse("add taskname from 2pm to 10pm");
		} catch (Exception e) {
			e.printStackTrace();
		}
		_controller.executeCommand(addCmd1);
		_controller.executeCommand(addCmd2);
		_controller.executeCommand(addCmd3);
		Task expectedTask3 = null;
		Task expectedTask2= null;
		if(LocalTime.now().isAfter(_today10pm.toLocalTime())){
			expectedTask2 = new Task("taskname", _today10pm.plusDays(1));
		}else{
			expectedTask2 = new Task("taskname",_today10pm);
		}
		if(LocalTime.now().isAfter(_today2pm.toLocalTime())){
			expectedTask3 = new Task("taskname",_today2pm.plusDays(1),_today10pm.plusDays(1));
		}else{
			expectedTask3 = new Task("taskname",_today2pm,_today10pm);
		}
		
		Task expectedTask1 = new Task("taskname");
		
		
		assertEquals("[" + expectedTask1.toString() + ", " + expectedTask2.toString()
				+ ", " + expectedTask3.toString() + "]", 
				_controller.getStorage().getTaskList().toString());
	}
	
	@Test
	public void testUpdateTaskname() throws TaskRetrievalFailedException {
		Command addCmd = null;
		Command displayCmd = null;
		Command updateCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname");
			displayCmd = _controller.getParser().parse("display");
			updateCmd = _controller.getParser().parse("update 1 newTaskname");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(updateCmd);
	
		Task expectedTask = new Task("newTaskname");
		assertEquals(expectedTask.toString(), _controller.getStorage().getTaskList().get(0).toString());
	}
	
	@Test
	public void testUpdateDateTime() throws TaskRetrievalFailedException {
		Command addCmd = null;
		Command displayCmd = null;
		Command updateCmd = null;
		
		LocalDateTime _tmr9pm = LocalDateTime.of(_now.toLocalDate().plusDays(1), LocalTime.of(21, 0, 0, 0));
		LocalDateTime _tmr3pm = LocalDateTime.of(_now.toLocalDate().plusDays(1), LocalTime.of(15,0,0,0));
		
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			displayCmd = _controller.getParser().parse("display");
			updateCmd = _controller.getParser().parse("update 1 from 3pm to 9pm tomorrow");	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(updateCmd);
		
		Task expectedTask = new Task("taskname", _tmr3pm, _tmr9pm);
		assertEquals(expectedTask.toString(), _controller.getStorage().getTaskList().get(0).toString());
	}
	
	@Test
	public void testMarkSingle() throws TaskRetrievalFailedException {
		Command addCmd = null;
		Command displayCmd = null;
		Command markCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname");
			displayCmd = _controller.getParser().parse("display");
			markCmd = _controller.getParser().parse("mark 1");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		assertEquals(false, _controller.getStorage().getTaskList().get(0).isDone());
		
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(markCmd);
		assertEquals(true, _controller.getStorage().getTaskList().get(0).isDone());
	}
	
	@Test
	public void testMarkMultiple() throws TaskRetrievalFailedException {
		Command addCmd1 = null;
		Command addCmd2 = null;
		Command addCmd3 = null;
		
		Command displayCmd = null;
		Command markCmd = null;
		
		try {
			addCmd1 = _controller.getParser().parse("add taskname");
			addCmd2 = _controller.getParser().parse("add taskname by 10pm");
			addCmd3 = _controller.getParser().parse("add taskname from 2pm to 10pm");
			displayCmd = _controller.getParser().parse("display");
			markCmd = _controller.getParser().parse("mark 1-3");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd1);
		_controller.executeCommand(addCmd2);
		_controller.executeCommand(addCmd3);
		assertEquals(false, _controller.getStorage().getTaskList().get(0).isDone());
		assertEquals(false, _controller.getStorage().getTaskList().get(1).isDone());
		assertEquals(false, _controller.getStorage().getTaskList().get(2).isDone());
		
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(markCmd);
		assertEquals(true, _controller.getStorage().getTaskList().get(0).isDone());
		assertEquals(true, _controller.getStorage().getTaskList().get(1).isDone());
		assertEquals(true, _controller.getStorage().getTaskList().get(2).isDone());
	}
	
	@Test
	public void testUndoOnce() throws TaskRetrievalFailedException, Exception {
		Command addCmd = null;
		Command undoCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			undoCmd = _controller.getParser().parse("undo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(undoCmd);
		assertEquals("[]", _controller.getStorage().getTaskList().toString());	
	}
	
	@Test
	public void testMultipleUndo() throws TaskRetrievalFailedException, Exception {
		Command addCmd = null;
		Command updateCmd = null;
		Command undoCmd = null;
		Command displayCmd = null;
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			displayCmd = _controller.getParser().parse("display");
			updateCmd = _controller.getParser().parse("update 1 from 3pm to 9pm tomorrow");
			undoCmd = _controller.getParser().parse("undo 2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(updateCmd);
		_controller.executeCommand(undoCmd);

		assertEquals("[]", _controller.getStorage().getTaskList().toString());
		
	}

	@Test
	public void testRedoOnce() throws TaskRetrievalFailedException, Exception {
		Command addCmd = null;
		Command undoCmd = null;
		Command redoCmd = null;
		
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			undoCmd = _controller.getParser().parse("undo");
			redoCmd = _controller.getParser().parse("redo");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Task expectedTask = null;
		if(LocalTime.now().isAfter(_today2pm.toLocalTime())){
			expectedTask = new Task("taskname",_today2pm.plusDays(1),_today10pm.plusDays(1));
		}else{
			expectedTask = new Task("taskname",_today2pm,_today10pm);
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(undoCmd);
		_controller.executeCommand(redoCmd);
		assertEquals("[" + expectedTask.toString() + "]", _controller.getStorage().getTaskList().toString());	
	}
	
	@Test
	public void testMultipleRedo() throws TaskRetrievalFailedException, Exception {
		Command addCmd = null;
		Command deleteCmd = null;
		Command undoCmd = null;
		Command redoCmd = null;
		Command displayCmd = null;
		try {
			addCmd = _controller.getParser().parse("add taskname from 2pm to 10pm");
			displayCmd = _controller.getParser().parse("display");
			deleteCmd = _controller.getParser().parse("delete 1");
			undoCmd = _controller.getParser().parse("undo 2");
			redoCmd= _controller.getParser().parse("redo 2");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		_controller.executeCommand(addCmd);
		_controller.executeCommand(displayCmd);
		_controller.executeCommand(deleteCmd);
		_controller.executeCommand(undoCmd);
		_controller.executeCommand(redoCmd);

		assertEquals("[]", _controller.getStorage().getTaskList().toString());
		
	}
	
	
	
	
}
