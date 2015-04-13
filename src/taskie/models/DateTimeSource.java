//@author A0121555M
package taskie.models;

import java.time.LocalDateTime;

public class DateTimeSource {
	private static LocalDateTime BASE_DATETIME = null;
	
	/**
	 * Set date and time for Taskie / Test cases to use
	 * 
	 * @param command	Fixed current Date and Time (or null to use the REAL current date and time)
	 */
	public static void setCurrentDateTime(LocalDateTime now) {
		BASE_DATETIME = now;
	}
	
	public static LocalDateTime getCurrentDateTime() {
		return BASE_DATETIME == null ? LocalDateTime.now() : BASE_DATETIME;
	}
}
