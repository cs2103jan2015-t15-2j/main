package taskie.ui;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RelativeDate {
	private LocalDateTime _relativeTo;
	private long _years;
	private long _months;
	private long _days;
	private long _hours;
	private long _minutes;
	private long _seconds;
	private boolean _ago;

	public RelativeDate() {
		_relativeTo = LocalDateTime.now();
	}

	public RelativeDate(LocalDateTime relativeto) {
		_relativeTo = relativeto;
	}
	
	public void setRelativeTo(LocalDateTime relativeto) {
		_relativeTo = relativeto;
	}

	public long getYears() {
		return _years;
	}

	public long getMonths() {
		return _months;
	}

	public long getDays() {
		return _days;
	}

	public long getHours() {
		return _hours;
	}

	public long getMinutes() {
		return _minutes;
	}

	public long getSeconds() {
		return _seconds;
	}

	public boolean isAgo() {
		return _ago;
	}

	public void calculate(LocalDateTime dateTime) {
		_ago = false;
		LocalDateTime relativeTo = _relativeTo;

		if (dateTime.isBefore(relativeTo)) {
			_ago = true;
			LocalDateTime temp = dateTime;
			dateTime = relativeTo;
			relativeTo = temp;
		}

		LocalDateTime difference = LocalDateTime.from(relativeTo);

		_years = difference.until(dateTime, ChronoUnit.YEARS);
		difference = difference.plusYears(_years);

		_months = difference.until(dateTime, ChronoUnit.MONTHS);
		difference = difference.plusMonths(_months);

		_days = difference.until(dateTime, ChronoUnit.DAYS);
		difference = difference.plusDays(_days);

		_hours = difference.until(dateTime, ChronoUnit.HOURS);
		difference = difference.plusHours(_hours);

		_minutes = difference.until(dateTime, ChronoUnit.MINUTES);
		difference = difference.plusMinutes(_minutes);

		_seconds = difference.until(dateTime, ChronoUnit.SECONDS);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		if (_years > 0) {
			sb.append(String.format("%d %s", _years, _years > 1 ? "years" : "year") + " ");
		}

		if (_months > 0) {
			sb.append(String.format("%d %s", _months, _months > 1 ? "months" : "month") + " ");
		}

		if (_days > 0) {
			sb.append(String.format("%d %s", _days, _days > 1 ? "days" : "day") + " ");
		}

		if (_hours > 0) {
			sb.append(String.format("%d %s", _hours, _hours > 1 ? "hours" : "hour") + " ");
		}

		if (_minutes > 0) {
			sb.append(String.format("%d %s", _minutes, _minutes > 1 ? "minutes" : "minute") + " ");
		}

		if (_seconds > 0) {
			sb.append(String.format("%d %s", _seconds, _seconds > 1 ? "seconds" : "second") + " ");
		}

		if (_ago) {
			sb.append("ago ");
		}

		return sb.toString().trim();
	}
}