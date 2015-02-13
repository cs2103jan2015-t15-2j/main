package taskie;

//responsible for storing tasklists in non-volatile form
public interface iStorage {
	String storageLocation=null;
	
	//return true if successful
	Boolean setStorageLocation(String fileDir);
	
	iTask[] retrieveTaskList();
	
	iTask[] storeTaskList();
}
