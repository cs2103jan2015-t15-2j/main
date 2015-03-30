//@author A0121555M
package taskie.commands;

import taskie.Controller;

public abstract class AbstractCommand implements ICommand {
	protected Controller _controller;

	public AbstractCommand() {
		_controller = Controller.getInstance();
	}
}
