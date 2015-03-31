//@author A0121555M
package taskie;

public class Taskie {
	public static void main(String[] args) {
		new Taskie(args);
	}

	public Taskie(String[] args) {
		try {
			Controller c = Controller.getInstance();
			c.run();
		} catch (Exception e) {
			System.out.println("Unknown Error: " + e.getMessage());
		}
	}
}