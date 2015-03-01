package taskie.models;

import java.time.LocalDateTime;
import java.util.Comparator;
import taskie.models.Task;

public class TaskStartDateComparator implements Comparator<Task> {

	public TaskStartDateComparator() {
	}

	@Override
	public int compare(Task o1, Task o2) {
		LocalDateTime o1DateTime = LocalDateTime.of(o1.getStartDate(),o1.getStartTime());
		LocalDateTime o2DateTime = LocalDateTime.of(o2.getStartDate(),o2.getStartTime());
		return o1DateTime.compareTo(o2DateTime);
	}

}

