//@author A0121555M
package taskie;

import java.io.IOException;

public class Taskie {
	public static taskie.controller.Controller Controller;

	public static void main(String[] args) {
		try {
			new Taskie(args);
		} catch (IOException e) {
			System.out.println("Unable to initialize Storage");
		} catch (Exception e) {
			System.out.println("Unknown Error: " + e.getMessage());
		}
	}

	public Taskie(String[] args) throws IOException {
		Controller = new taskie.controller.Controller();
		Controller.run();
	}
}