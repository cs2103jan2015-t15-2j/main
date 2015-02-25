//@author A0121555M
package taskie;

import taskie.database.IStorage;
import taskie.ui.CommandUI;
import taskie.ui.UI;

public class Taskie {
	public static UI UI;
<<<<<<< HEAD
	private IStorage _storage;
=======
	public static taskie.controller.Controller Controller;
	
>>>>>>> upstream/master
	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Taskie(String[] args) {
		Controller = new taskie.controller.Controller();
		UI = new CommandUI();
		UI.run();
	}
	
	IStorage getStorage(){
		return _storage;
		
	}
}
