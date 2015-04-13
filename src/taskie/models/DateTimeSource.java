//@author A0121555M
package taskie.models;

import java.time.LocalDateTime;

public class DateTimeSource {
	private static LocalDateTime BASE_DATETIME = null;
	
	public static void setCurrentDateTime(LocalDateTime now) {
		BASE_DATETIME = now;
	}
	
	public static LocalDateTime getCurrentDateTime() {
		return BASE_DATETIME == null ? LocalDateTime.now() : BASE_DATETIME;
	}
}
