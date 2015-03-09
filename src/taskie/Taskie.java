//@author A0121555M
package taskie;

import taskie.database.IStorage;
import taskie.ui.CommandUI;
import taskie.ui.UI;
import taskie.parser.CommandParser;
import taskie.parser.Parser;

public class Taskie {
	public static UI UI;
	public static taskie.controller.Controller Controller;
	public static IStorage Storage;
	public static Parser Parser;
	
	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Taskie(String[] args) {
		Controller = new taskie.controller.Controller();
		Storage = new taskie.database.Storage();
		UI = new CommandUI();
		Parser = new CommandParser();
		Controller.run();
	}
}
