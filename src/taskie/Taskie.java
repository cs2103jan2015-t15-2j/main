//@author A0121555M
package taskie;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Taskie {
	private static String LOGGER_FILENAME = "taskie-log.txt";
	public static void main(String[] args) {
		configureLogger();
		new Taskie(args);
	}
	
	private static void configureLogger() {
		FileHandler fh;
		try {
			LogManager.getLogManager().reset();

			fh = new FileHandler("%t/" + LOGGER_FILENAME);
			Logger.getLogger("").addHandler(fh);
		} catch (SecurityException e) {
			System.out.println("SecurityException: Unable to setup logging.");
		} catch (IOException e) {
			System.out.println("IOException: Unable to setup logging.");
		}
	}

	public Taskie(String[] args) {
		try {
			Controller c = Controller.getInstance();
			c.run();
		} catch (Exception e) {
			System.out.println("Unknown Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}