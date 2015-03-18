//@author A0121555M
package taskie;

public class Taskie {
	public static taskie.controller.Controller Controller;

	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Taskie(String[] args) {
		Controller = new taskie.controller.Controller();
		Controller.run();
	}
}