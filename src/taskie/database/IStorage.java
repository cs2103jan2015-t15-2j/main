//@author	A0135137L

package taskie.database;
import taskie.models.Task;
import java.util.ArrayList;
import java.util.HashMap;


public interface IStorage {

	
	void setStorageLocation(String fileDir); //done
	
	HashMap<String, ArrayList<Task>> retrieveTaskMap(); //done
	

	void storeTaskMap(HashMap<String, ArrayList<Task>> hm);  //done


	void addFloatingTask(Task taskToAdd); //done


	void addDeadlinedTask(Task taskToAdd); //done


	void addTimedTask(Task taskToAdd); //done

}
