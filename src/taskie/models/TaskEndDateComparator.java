package taskie.models;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TaskEndDateComparator implements Comparator<Task> {

	public TaskEndDateComparator() {
		
	}

	@Override
	public int compare(Task o1, Task o2) {
		LocalDateTime o1DateTime = LocalDateTime.of(o1.getEndDate(),o1.getEndTime());
		LocalDateTime o2DateTime = LocalDateTime.of(o2.getEndDate(),o2.getEndTime());
		return o1DateTime.compareTo(o2DateTime);
	}

}
