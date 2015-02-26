/**
 * Class to perform tasks needed to achieve functionality. Interface between front and back end.
 * Still under development
 * Bugs: none known
 *
 */
//@author       A0097582N

package taskie.controller;

import taskie.*;
import taskie.commands.ICommand;

public class Controller {

	public Controller() {
	}

	public void executeCommand(ICommand command) {
		command.execute();
		

	}

}