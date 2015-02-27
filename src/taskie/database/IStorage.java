package taskie.database;
//@author	A0097582N

import taskie.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

//responsible for storing tasklists in non-volatile form
public interface IStorage {

	
	void setStorageLocation(String fileDir);
	
	HashMap<String, ArrayList<Task>> retrieveTaskMap(); //done
	HashMap<String, ArrayList<Task>> retrieveTaskMap(String fileDir);
	
	boolean storeTaskLists(HashMap<String, ArrayList<Task>> tasklists, String fileDir);
	boolean storeTaskLists(HashMap<String, ArrayList<Task>> tasklists);

	boolean addFloatingTask(Task taskToAdd);
	boolean addFloatingTask(Task taskToAdd,String fileDir);

	boolean addDeadlineTask(Task taskToAdd);
	boolean addDeadlineTask(Task taskToAdd,String fileDir);

	boolean addTimedTask(Task taskToAdd);
	boolean addTimedTask(Task taskToAdd,String fileDir);
}
