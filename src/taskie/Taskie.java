//@author A0121555M
package taskie;

import taskie.ui.CommandUI;
import taskie.ui.UI;

public class Taskie {
	private UI ui;
	
	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Taskie(String[] args) {
		ui = new CommandUI();
		ui.run();
	}
}
