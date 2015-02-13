package taskie.database;

import taskie.models.Task;

//responsible for storing tasklists in non-volatile form
public interface IStorage {
	String storageLocation=null;
	
	//return true if successful
	Boolean setStorageLocation(String fileDir);
	
	Task[] retrieveTaskList();
	
	Task[] storeTaskList();
}
