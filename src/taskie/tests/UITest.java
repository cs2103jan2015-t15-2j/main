//@author A0121555M
package taskie.tests;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.Controller;
import taskie.models.Task;
import taskie.ui.CommandUI;
import taskie.ui.UI;

public class UITest {
	private UI UI;
	private static ArrayList<Task> tasks;
	private static Controller _controller;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		_controller = Controller.getInstance();
		tasks = new ArrayList<Task>();
		
		tasks.add(new Task("Have Dinner"));
		tasks.add(new Task("Attend CS2103 Meeting"));
	}

	@Before
	public void setUp() throws Exception {
		UI = new CommandUI(_controller);
	}

	@Test
	public void testDisplay() {
		UI.display(tasks.toArray(new Task[tasks.size()]));
	}

}
