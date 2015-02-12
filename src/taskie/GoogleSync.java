package taskie;

public interface GoogleSync {
	
	//retrieve tasks from Google
	Boolean syncTo(Task[] tasksToSync);
	
	//upload tasks to google
	Task[] syncFrom();
	
}
