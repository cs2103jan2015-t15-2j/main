package taskie;

//responsible for authenticating, syncing with google.
public interface iGoogleSync {
	//if input account is valid
	boolean isValidAccount = false;
	
	//retrieve tasks from Google
	Boolean syncTo(iTask[] tasksToSync);
	
	//upload tasks to google
	iTask[] syncFrom();
	
}
