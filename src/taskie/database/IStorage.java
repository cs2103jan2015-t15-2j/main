package taskie.database;
//@author	A0097582N

import taskie.models.Task;

import java.util.ArrayList;
import java.util.HashMap;

//responsible for storing tasklists in non-volatile form
public interface IStorage {

	
	void setStorageLocation(String fileDir); //done
	
	HashMap<String, ArrayList<Task>> retrieveTaskMap(); //done
	HashMap<String, ArrayList<Task>> retrieveTaskMap(String fileDir); 
	

	boolean storeTaskMap(HashMap<String, ArrayList<Task>> hm, String fileDir);
	void storeTaskMap(HashMap<String, ArrayList<Task>> hm);  //done


	void addFloatingTask(Task taskToAdd); //done
	boolean addFloatingTask(Task taskToAdd,String fileDir);

	void addDeadlinedTask(Task taskToAdd); //done
	boolean addDeadlinedTask(Task taskToAdd,String fileDir);

	void addTimedTask(Task taskToAdd); //done
	boolean addTimedTask(Task taskToAdd,String fileDir);
}
