package taskie;

public interface iGoogleSync {
	
	//retrieve tasks from Google
	Boolean syncTo(iTask[] tasksToSync);
	
	//upload tasks to google
	iTask[] syncFrom();
	
}
