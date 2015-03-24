package taskie.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import taskie.Taskie;
import taskie.commands.*;
import taskie.controller.Controller;
import taskie.database.IStorage;
import taskie.exceptions.InvalidTaskException;
import taskie.models.Task;
import taskie.models.TaskType;
import taskie.ui.UI;

public class ControllerTest {
	Taskie _taskie;
	Controller _controller;
	String _uiDisplayStr;
	Task[] _uiDisplayTasks;
	Task _uiDisplayTask;
	
	
	Task _storageTask;
	

	@Before
	public void setUp() throws Exception {
		_controller = Taskie.Controller;
		_controller.setUI(new StubUI());
		_controller.setStorage(new StubStorage());
		_uiDisplayStr=null;
		_uiDisplayTasks=null;
		_uiDisplayTask=null;
		_storageTask=null;
		
	}

	@Test
	public void test_add_floating() {
		AddCommand command = new AddCommand();
		command.setTaskName("add floating");
		_controller.executeCommand(command);
		Assert.assertEquals("floating added.",_uiDisplayStr);
		
	}

	private class StubUI implements UI{
		public StubUI() {
		}
	
		@Override
		public void run() {
			
			
		}
	
		@Override
		public String readInput() {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public void display(Task task) {
			_uiDisplayTask =task;
			
		}
	
		@Override
		public void display(Task[] tasks) {
			_uiDisplayTasks=tasks;
			
		}
	
		@Override
		public void display(String message) {
			_uiDisplayStr = message;
			
		}
	
		@Override
		public void exit() {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public Task[] getCurrentTaskList() throws InvalidTaskException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Task getTask(int index) throws InvalidTaskException {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public boolean isUIRunning() {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public String loadSelectDirectoryDialog(String currentDirectory) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public class StubStorage implements IStorage{
		public StubStorage(){

	}

		@Override
		public String getStorageLocation() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setStorageLocation(String fileDir) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public HashMap<TaskType, ArrayList<Task>> retrieveTaskMap() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void storeTaskMap(HashMap<TaskType, ArrayList<Task>> hm) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addFloatingTask(Task taskToAdd) {
			_storageTask=taskToAdd;
			
		}

		@Override
		public void addDeadlinedTask(Task taskToAdd) {
			_storageTask=taskToAdd;
			
		}

		@Override
		public void addTimedTask(Task taskToAdd) {
			_storageTask=taskToAdd;
			
		}

		@Override
		public void deleteFloatingTask(Task taskToDelete) {
			_storageTask=taskToDelete;
			
		}

		@Override
		public void deleteDeadlinedTask(Task taskToDelete) {
			_storageTask=taskToDelete;
			
		}

		@Override
		public void deleteTimedTask(Task taskToDelete) {
			_storageTask=taskToDelete;
			
		}

		@Override
		public void updateTask(Task oldTask, Task newTask) {
			_storageTask=newTask;
			
		}
	}
	
}
