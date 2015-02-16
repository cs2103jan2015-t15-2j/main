//@author A0121555M
package taskie;

import taskie.ui.CommandUI;
import taskie.ui.UI;

public class Taskie {
	public static UI UI;
	
	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Taskie(String[] args) {
		UI = new CommandUI();
		UI.run();
	}
}
