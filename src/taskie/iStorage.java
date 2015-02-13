package taskie;

public interface iStorage {
	String storageLocation=null;
	
	
	Boolean setStorageLocation(String fileDir);
	
	iTask[] retrieveTaskList();
	
	iTask[] storeTaskList();
}
