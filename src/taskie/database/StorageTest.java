package taskie.database;

import java.util.ArrayList;

import taskie.models.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class StorageTest {
	public static void main(String[] args) {
	
		
		/*
		 * Test for storeTaskMap()
		 * 
		Storage stor = new Storage();
		ArrayList<Task> deadlinedTasks = new ArrayList<Task>();
		ArrayList<Task> timedTasks = new ArrayList<Task>();
		ArrayList<Task> floatingTasks = new ArrayList<Task>();
		HashMap hm = new HashMap<String,ArrayList<Task>>();

		LocalDate dlTask1Ld = LocalDate.of(2015, 2, 10);
		LocalDate dlTask2Ld = LocalDate.of(2015, 2, 11);
		LocalTime dlTask2Lt = LocalTime.of(15, 00);
		Task dlTask1 = new Task("deadline1", dlTask1Ld);
		Task dlTask2 = new Task("deadline2", dlTask2Ld, dlTask2Lt);

		LocalDate tTask3sd = LocalDate.of(2015, 2, 12);
		LocalDate tTask3ed = LocalDate.of(2015, 2, 13);
		LocalDate tTask4sd = LocalDate.of(2015, 2, 13);
		LocalDate tTask4ed = LocalDate.of(2015, 2, 15);
		LocalTime tTask4st = LocalTime.of(16, 00);
		LocalTime tTask4et = LocalTime.of(10, 00);
		Task tTask3 = new Task("timed3", tTask3sd, tTask3ed);
		Task tTask4 = new Task("timed4", tTask4sd, tTask4st, tTask4ed, tTask4et);

		Task fTask5 = new Task("time5");
		Task fTask6 = new Task("time6");
		
		deadlinedTasks.add(dlTask1);
		deadlinedTasks.add(dlTask2);
		timedTasks.add(tTask3);
		timedTasks.add(tTask4);
		floatingTasks.add(fTask5);
		floatingTasks.add(fTask6);
		
		hm.put(Storage.DEADLINED_TASKNAME, deadlinedTasks);
		hm.put(Storage.TIMED_TASKNAME, timedTasks);
		hm.put(Storage.FLOATING_TASKNAME, floatingTasks);
		
		stor.storeTaskMap(hm);  
		*/
		
		
		
		
		/*
		 * Test for retrieveTaskMap() 
		 *
		Storage stor = new Storage();
		HashMap<String,ArrayList<Task>> hm = stor.retrieveTaskMap();
		for(Task t: hm.get(Storage.TIMED_TASKNAME)){
			System.out.println(t.getStartDate());
		}
		
		*/

		
		
		/*
		 * Test for addTimedTask()
		 * */
		Storage stor = new Storage();
		LocalDate tTask7sd = LocalDate.of(2015, 1, 11);
		LocalDate tTask7ed = LocalDate.of(2015, 1, 15);
		LocalTime tTask7st = LocalTime.of(14, 00);
		LocalTime tTask7et = LocalTime.of(5, 00);
		Task tTask7 = new Task("timed7", tTask7sd, tTask7st, tTask7ed, tTask7et);
		stor.addTimedTask(tTask7);
		
		/**/
		
		
	
	}

}
