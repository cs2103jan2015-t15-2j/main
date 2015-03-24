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
	class StubController extends Controller {
		//set method for replacing component with stub component
		public void setUI(UI ui){
			_ui = ui;
		}
		//set method for replacing component with stub component
		public void setStorage(IStorage storage){
			_storage = storage;
		}
	}
	
	Taskie _taskie;
	StubController _controller;
	String _uiDisplayStr;
	Task[] _uiDisplayTasks;
	Task _uiDisplayTask;
	
	
	Task _storageTask;
	

	@Before
	public void setUp() throws Exception {
		_controller = new StubController();
		_controller.setUI(new StubUI());
		_controller.setStorage(new StubStorage());
		_uiDisplayStr=null;
		_uiDisplayTasks=null;
		_uiDisplayTask=null;
		_storageTask=null;
		
	}

	@Test
	public void test_add_floating() {



	
}
