package taskie.gsync;

import taskie.models.Task;

//responsible for authenticating, syncing with google.
public interface iGoogleSync {
	//if input account is valid
	boolean isValidAccount = false;
	
	//retrieve tasks from Google
	Boolean syncTo(Task[] tasksToSync);
	
	//upload tasks to google
	Task[] syncFrom();
	
}
