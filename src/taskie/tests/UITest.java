package taskie.tests;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import taskie.models.Task;
import taskie.ui.CommandUI;
import taskie.ui.UI;

public class UITest {
	private UI UI;
	private static ArrayList<Task> tasks;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		tasks = new ArrayList<Task>();
		
		tasks.add(new Task("Have Dinner"));
		tasks.add(new Task("Attend CS2103 Meeting"));
	}

	@Before
	public void setUp() throws Exception {
		UI = new CommandUI();
	}

	@Test
	public void testDisplay() {
		UI.display(tasks.toArray(new Task[tasks.size()]));
	}

}
