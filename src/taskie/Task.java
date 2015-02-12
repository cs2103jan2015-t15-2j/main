package taskie;

import java.util.Calendar;

public interface Task {
	//private variables
	String _title = null;
	Calendar _startTime=null;
	Calendar _endTime=null;
	String _description=null;
	
	
	String getTitle();
	String getStartTime();
	String getEndTime();
	String getDescription();
}
