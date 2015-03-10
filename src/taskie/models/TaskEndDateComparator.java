package taskie.models;

//@author A0097582N
import java.time.LocalDateTime;
import java.util.Comparator;

public class TaskEndDateComparator implements Comparator<Task> {

	public TaskEndDateComparator() {
		
	}

	@Override
	public int compare(Task o1, Task o2) {
		//TODO handles null time
		if(o1.getEndDate()==null){
			if(o2.getEndDate()==null){
				return 0;
			}
			else{
				return 1;
			}
		}else{
			if(o2.getEndDate()==null){
				return 1;
			}
		}
		LocalDateTime o1DateTime = o1.getEndDateTime();
		LocalDateTime o2DateTime = o2.getEndDateTime();
		return o1DateTime.compareTo(o2DateTime);
	}

}
