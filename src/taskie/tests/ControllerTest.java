package taskie.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import taskie.commands.*;
import taskie.controller.Controller;

public class ControllerTest {
	Controller controller;

	@Before
	public void setUp() throws Exception {
		controller = new Controller();
	}

	@Test
	public void test_add_floating() {
		AddCommand command = new AddCommand();
		command.setTaskName("add floating");
		controller.executeCommand(command);
		
	}

}
